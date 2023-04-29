package com.sanryoo.matcher.features.presentation.screen.using.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.sanryoo.matcher.features.data.api.ApiToken
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.usecase.UseCases
import com.sanryoo.matcher.features.presentation.screen.using.message.MessageState
import com.sanryoo.matcher.features.util.MyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val useCases: UseCases,
    private val signInClient: GoogleSignInClient,
    private val _messageState: MutableStateFlow<MessageState>,
    @Named(value = MyString.USER) private val _user: MutableStateFlow<User>,
    @Named(value = MyString.OTHERS) private val _others: MutableStateFlow<User>,
) : ViewModel() {

    val user = _user.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object ChangePassword : UiEvent()
        object About : UiEvent()
        object LogOut : UiEvent()
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun logOut() {
        _others.value = User()
        _messageState.value = MessageState()
        ApiToken.token.value = ""

        signInClient.signOut() //Log out google account
        LoginManager.getInstance().logOut() //Log out facebook account

        CoroutineScope(Dispatchers.Main).launch {
            try {
                useCases.updateUser(_user.value.copy(fcmtoken = ""))
                delay(500L)
                _user.value = User()
            } catch (e: Exception) {
                _user.value = User()
                return@launch
            }
        }

        onUiEvent(UiEvent.LogOut)
    }
}