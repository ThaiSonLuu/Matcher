package com.sanryoo.matcher.features.presentation.screen.using.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.domain.location.hasLocationPermission
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.model.send_and_response_data.MatchData
import com.sanryoo.matcher.features.domain.usecase.UseCases
import com.sanryoo.matcher.features.domain.websocketclient.WebSocketClient
import com.sanryoo.matcher.features.presentation.screen.using.message.MessageState
import com.sanryoo.matcher.features.util.MyString
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val useCases: UseCases,
    private val stompClient: WebSocketClient,
    private val _reLogin: MutableSharedFlow<Unit>,
    private val _messageState: MutableStateFlow<MessageState>,
    _visibleSplashScreen: MutableStateFlow<Boolean>,
    @Named(value = MyString.USER) private val _user: MutableStateFlow<User>,
    @Named(value = MyString.OTHERS) private val _others: MutableStateFlow<User>,
) : ViewModel() {

    val user = _user.asStateFlow()
    val others = _others.asStateFlow()

    private val _tempOthers = MutableStateFlow(User())
    val tempOthers = _tempOthers.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var listenResponseFind: Disposable? = null
    private var listenResponseConfirm: Disposable? = null

    sealed class UiEvent {
        data class Navigate(val route: String) : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }

    init {
        _visibleSplashScreen.value = false
    }

    fun onUiEvent(event: UiEvent) { viewModelScope.launch { _eventFlow.emit(event) } }

    @SuppressLint("CheckResult")
    fun find() {
        if (!checkInformationBeforeFind()) {
            onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.you_must_fill_in_full_information)))
        } else if (!application.hasLocationPermission()) {
            onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.you_must_allow_access_location_permission)))
        } else {
            try {
                listenFind()
                listenConfirm()
                updateInformation(
                    information = user.value.copy(searching = 1),
                    onConnectError = {
                        _user.value = user.value.copy(searching = 0)
                        onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.can_not_find)))
                    },
                    onSuccess = {
                        stompClient.send("/send/find", Gson().toJson(user.value)).subscribe(
                            {},
                            { onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.can_not_find))) }
                        )
                    })
            } catch (e: Exception) {
                _user.value = user.value.copy(searching = 0)
                onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.can_not_find)))
                return
            }
        }
    }

    fun cancelFind() {
        try {
            _user.value = user.value.copy(searching = 0)
            updateInformation(user.value)
            listenResponseFind?.dispose()
            listenResponseConfirm?.dispose()
        } catch (e: Exception) {
            return
        }
    }

    private fun listenFind() {
        listenResponseFind =
            stompClient.join("/response/find/${user.value.id}").subscribe({ message ->
                _user.value = user.value.copy(searching = 0)
                val dataResponse = Gson().fromJson(message, User::class.java)
                if (dataResponse.id > 0) {
                    _tempOthers.value = dataResponse
                } else {
                    onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.not_found_anyone)))
                }
                listenResponseFind?.dispose()
            }, {}
            )
    }

    private fun listenConfirm() {
        listenResponseConfirm =
            stompClient.join("/response/confirm/${user.value.id}").subscribe({ message ->
                viewModelScope.launch {
                    delay(1000L)
                    if (message.last() == '1') {
                        _user.value = user.value.copy(
                            matched = tempOthers.value.id,
                            currentdistance = tempOthers.value.currentdistance
                        )
                        updateInformation(user.value)
                        _others.value = tempOthers.value
                        _tempOthers.value = User()
                        getMessage()
                        _eventFlow.emit(UiEvent.ShowSnackBar("${application.getString(R.string.you_have_been_matched_with)} ${others.value.fullname}"))
                    } else {
                        _tempOthers.value = User()
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.the_match_has_been_canceled)))
                    }
                    listenResponseConfirm?.dispose()
                }
            }, {}
            )
    }

    @SuppressLint("CheckResult")
    fun confirm(data: Int) {
        val confirmData = if (user.value.id < tempOthers.value.id) {
            MatchData(ids = "${user.value.id}_${tempOthers.value.id}", data = data)
        } else {
            MatchData(ids = "${tempOthers.value.id}_${user.value.id}", data = data)
        }
        stompClient.send("/send/confirm", Gson().toJson(confirmData)).subscribe({
            if (data == -1) {
                _tempOthers.value = User()
                onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.the_match_has_been_canceled)))
                listenResponseConfirm?.dispose()
            }
        }, {})
    }

    private fun updateInformation(
        information: User,
        onConnectError: () -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val response = useCases.updateUser(information)
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.code == 200 && item.status == "ok") {
                        _user.value = item.data
                        onSuccess()
                    } else if (item?.status == "failed" && item.code == 401) {
                        _reLogin.emit(Unit)
                    }
                }
            } catch (e: IOException) {
                onConnectError()
                return@launch
            } catch (e: HttpException) {
                onConnectError()
                return@launch
            } catch (e: SocketTimeoutException) {
                onConnectError()
                return@launch
            } catch (e: Exception) {
                return@launch
            }
        }
    }

    private fun getMessage() {
        if (_user.value.matched > 0) {
            viewModelScope.launch {
                try {
                    val response =
                        useCases.getMessages(user.value.id, others.value.id, 0, 59)
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

    private fun checkInformationBeforeFind() =
        user.value.avatar != "" && user.value.image1 != "" && user.value.image2 != "" && user.value.image3 != "" && user.value.fullname != "" && user.value.sex > 0 && user.value.age > 0 && user.value.height > 0 && user.value.weight > 0 && user.value.appearance > 0 && (user.value.easy == 1 || user.value.age1 > 0 && user.value.age2 > user.value.age1 && user.value.height1 > 0 && user.value.height2 > user.value.height1 && user.value.weight1 > 0 && user.value.weight2 > user.value.weight1 && user.value.appearance1 > 0)


}