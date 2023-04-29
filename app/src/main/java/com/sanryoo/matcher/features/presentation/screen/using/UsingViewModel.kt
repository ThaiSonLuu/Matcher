package com.sanryoo.matcher.features.presentation.screen.using

import android.app.Application
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.data.api.ApiToken
import com.sanryoo.matcher.features.domain.connectivity.NetworkConnectivityObserve
import com.sanryoo.matcher.features.domain.location.LocationClient
import com.sanryoo.matcher.features.domain.model.Message
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.usecase.UseCases
import com.sanryoo.matcher.features.domain.websocketclient.Event
import com.sanryoo.matcher.features.domain.websocketclient.WebSocketClient
import com.sanryoo.matcher.features.presentation.screen.using.message.MessageState
import com.sanryoo.matcher.features.util.MyString
import com.sanryoo.matcher.features.domain.datastore.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UsingViewModel @Inject constructor(
    private val application: Application,
    private val useCases: UseCases,
    private val stompClient: WebSocketClient,
    private val locationClient: LocationClient,
    private val connectivityObserve: NetworkConnectivityObserve,
    private val _messageState: MutableStateFlow<MessageState>,
    private val _reLogin: MutableSharedFlow<Unit>,
    @Named(value = MyString.USER) private val _user: MutableStateFlow<User>,
    @Named(value = MyString.OTHERS) private val _others: MutableStateFlow<User>,
    @Named(value = MyString.SENDING_MESSAGE) private val _sending: MutableStateFlow<Int>
) : ViewModel() {

    private val _reLogging = MutableStateFlow(false)
    val reLogging = _reLogging.asStateFlow()

    private val _dialogReLogin = MutableStateFlow(false)
    val dialogReLogin = _dialogReLogin.asStateFlow()

    private val _inputPassword = MutableStateFlow("")
    val inputPassword = _inputPassword.asStateFlow()

    private var connect: Disposable? = null
    private var listenResponseMessage: Disposable? = null
    private var listenResponseCancelMatch: Disposable? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object NavigateHome : UiEvent()
        object LaunchLocationPermission : UiEvent()
        data class ShowSnackBar(val message: String, val actionLabel: String? = null) : UiEvent()
    }

    companion object {
        private val fcmTokenKey = stringPreferencesKey("fcm_token")
    }

    init {
        getOthers()
        getMessage()
        updateFCMToken()
        connectWebSocket()
        observeConnectivity()
        viewModelScope.launch {
            delay(500L)
            _eventFlow.emit(UiEvent.LaunchLocationPermission)
        }
        viewModelScope.launch {
            _reLogin.collectLatest {
                if (_user.value.idgoogle != "" || _user.value.idfacebook != "") {
                    reLogIn()
                } else {
                    _dialogReLogin.value = true
                }
            }
        }
    }

    fun onInputPasswordChange(newInput: String) {
        _inputPassword.value = newInput
    }

    fun updateLocation() {
        locationClient.getLocationUpdates(5000L).catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude
                _user.value = _user.value.copy(latitude = lat, longitude = long)
                updateInformation()
            }.launchIn(viewModelScope)
    }

    private fun observeConnectivity() {
        connectivityObserve.observe()
            .drop(1)
            .catch { e -> e.printStackTrace() }
            .onEach { status ->
                if (status == NetworkConnectivityObserve.Companion.Status.Available) {
                    connectWebSocket()
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                application.getString(R.string.your_internet_connection_was_restored),
                                MyString.AVAILABLE
                            )
                        )
                    }
                } else {
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                application.getString(R.string.you_are_currently_offline),
                                MyString.NOT_AVAILABLE
                            )
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun connectWebSocket() {
        try {
            listenResponseMessage?.dispose()
            listenResponseCancelMatch?.dispose()
            connect?.dispose()
            connect = stompClient.connect().subscribe { event ->
                when (event) {
                    Event.OPENED -> {
                        listenMessage()
                        listenCancelMatch()
                    }
                    Event.CLOSED -> {}
                    Event.ERROR -> {}
                }
            }
        } catch (e: Exception) {
            return
        }
    }

    private fun listenMessage() {
        listenResponseMessage =
            stompClient.join("/response/message/${_user.value.id}").subscribe({ response ->
                val message = Gson().fromJson(response, Message::class.java)
                _messageState.value =
                    _messageState.value.copy(listMessage = listOf(message) + _messageState.value.listMessage)
                if (message.usersend.id == _user.value.id) {
                    _sending.value = 2
                } else {
                    _sending.value = 0
                }
            }, {}
            )
    }

    private fun listenCancelMatch() {
        listenResponseCancelMatch =
            stompClient.join("/response/cancel_match/${_user.value.id}").subscribe({
                _others.value = User()
                _user.value = _user.value.copy(matched = 0, currentdistance = 0.0)
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateHome)
                    delay(500L)
                    _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.the_match_has_been_canceled)))
                }
            }, {})
    }

    private fun getOthers() {
        if (_user.value.matched > 0) {
            viewModelScope.launch {
                try {
                    val response = useCases.getUser(_user.value.matched)
                    if (response.isSuccessful) {
                        val item = response.body()
                        if (item?.status == "ok" && item.code == 200) {
                            _others.value = item.data
                        } else if (item?.status == "failed" && item.code == 401) {
                            _reLogin.emit(Unit)
                        }
                    }
                } catch (e: Exception) {
                    return@launch
                }
            }
        }
    }

    private fun getMessage() {
        if (_user.value.matched > 0) {
            viewModelScope.launch {
                try {
                    val response = useCases.getMessages(
                        _user.value.id, _user.value.matched, 0, 39
                    )
                    if (response.isSuccessful) {
                        val item = response.body()
                        if (item?.status == "ok" && item.code == 200) {
                            _messageState.value = _messageState.value.copy(listMessage = item.data)
                        }
                    }
                } catch (e: Exception) {
                    return@launch
                }
            }
        }
    }

    private fun updateFCMToken() {
        viewModelScope.launch {
            val get = application.dataStore.data.first()
            val fcmToken = get[fcmTokenKey] ?: ""
            _user.value = _user.value.copy(fcmtoken = fcmToken)
            updateInformation()
        }
    }

    fun hideDialogReLogin() {
        _dialogReLogin.value = false
    }

    fun reLogIn(password: String = "") {
        viewModelScope.launch {
            try {
                _reLogging.value = true
                ApiToken.token.value = ""
                val response = useCases.logIn(_user.value.copy(password = password))
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.code == 200 && item.status == "ok") {
                        ApiToken.token.value = item.data.token
                        if (_user.value.idgoogle != "" || _user.value.idfacebook != "") {
                            delay(1000)
                        }
                        _reLogging.value = false
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.log_in_successful)))
                    }
                }
            } catch (e: Exception) {
                _reLogging.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.log_in_failed)))
                return@launch
            }
        }
    }

    private fun updateInformation() {
        viewModelScope.launch {
            try {
                val response = useCases.updateUser(_user.value)
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.status == "failed" && item.code == 401) { //Token was expired
                        _reLogin.emit(Unit)
                    }
                }
            } catch (e: Exception) {
                return@launch
            }
        }
    }

}