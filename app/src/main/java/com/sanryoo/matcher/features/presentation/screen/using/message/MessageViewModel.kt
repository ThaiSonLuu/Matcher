package com.sanryoo.matcher.features.presentation.screen.using.message

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.domain.model.Message
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.model.send_and_response_data.MatchData
import com.sanryoo.matcher.features.domain.usecase.UseCases
import com.sanryoo.matcher.features.domain.websocketclient.WebSocketClient
import com.sanryoo.matcher.features.util.MessageType
import com.sanryoo.matcher.features.util.MyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val application: Application,
    private val useCases: UseCases,
    private val stompClient: WebSocketClient,
    private val _messageState: MutableStateFlow<MessageState>,
    private val _reLogin: MutableSharedFlow<Unit>,
    @Named(value = MyString.USER) private val _user: MutableStateFlow<User>,
    @Named(value = MyString.OTHERS) private val _others: MutableStateFlow<User>,
    @Named(value = MyString.SENDING_MESSAGE) private val _sending: MutableStateFlow<Int>,
) : ViewModel() {

    val user = _user.asStateFlow()
    val others = _others.asStateFlow()

    val messageState = _messageState.asStateFlow()

    val sending = _sending.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val evenFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object NavigateBack : UiEvent()
        object ScrollToBottom : UiEvent()
        object NavigateOtherInformation : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun onInputMessageChange(newInput: String) {
        _messageState.value = messageState.value.copy(inputMessage = newInput)
    }

    fun loadMoreMessage() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val start = messageState.value.listMessage.size.toLong()
                val end = start + 19
                val response = useCases.getMessages(
                    user.value.id, others.value.id, start, end
                )
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.code == 200 && item.status == "ok") {
                        _messageState.value = messageState.value.copy(
                            listMessage = messageState.value.listMessage + item.data
                        )
                        delay(300L)
                        _isLoading.value = false
                    } else if (item?.status == "failed" && item.code == 401) {
                        _reLogin.emit(Unit)
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                return@launch
            }
        }
    }

    @SuppressLint("CheckResult")
    fun sendMessage() {
        val message = Message(
            usersend = user.value,
            userreceive = others.value,
            type = MessageType.TEXT,
            data = messageState.value.inputMessage
        )
        _messageState.value = messageState.value.copy(inputMessage = "")
        stompClient.send("/send/message", Gson().toJson(message)).subscribe({
            _sending.value = 1
            onUiEvent(UiEvent.ScrollToBottom)
        }, {
            viewModelScope.launch {
                _sending.value = 3
                onUiEvent(UiEvent.ScrollToBottom)
                delay(5000L)
                _sending.value = 0
            }
        })
    }

    fun sendMessageImage(uri: Uri) {
        viewModelScope.launch {
            try {
                _sending.value = 1
                onUiEvent(UiEvent.ScrollToBottom)
                useCases.postImageMessage(user.value.id, others.value.id, uri, application)
            } catch (e: IOException) {
                _sending.value = 3
                onUiEvent(UiEvent.ScrollToBottom)
                delay(5000L)
                _sending.value = 0
                return@launch
            } catch (e: HttpException) {
                _sending.value = 3
                onUiEvent(UiEvent.ScrollToBottom)
                delay(5000L)
                _sending.value = 0
                return@launch
            } catch (e: SocketTimeoutException) {
                _sending.value = 3
                onUiEvent(UiEvent.ScrollToBottom)
                delay(5000L)
                _sending.value = 0
                return@launch
            } catch (e: Exception) {
                return@launch
            }
        }
    }

    @SuppressLint("CheckResult")
    fun cancelMatch(data: Int) {
        try {
            val ids = if (user.value.id < others.value.id) {
                "${user.value.id}_${others.value.id}"
            } else {
                "${others.value.id}_${user.value.id}"
            }
            val cancelMatchData =
                MatchData(ids = ids, idsend = user.value.id, data = data)
            stompClient.send("/send/cancel_match", Gson().toJson(cancelMatchData)).subscribe(
                {}, {
                    onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.can_not_cancel_match_due_to_connection_error)))
                }
            )
        } catch (e: Exception) {
            return
        }
    }
}