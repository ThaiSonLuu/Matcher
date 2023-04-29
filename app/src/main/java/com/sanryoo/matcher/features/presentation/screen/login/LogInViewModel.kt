package com.sanryoo.matcher.features.presentation.screen.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.data.api.ApiToken
import com.sanryoo.matcher.features.domain.model.Token
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.usecase.UseCases
import com.sanryoo.matcher.features.util.MyString
import com.sanryoo.matcher.features.domain.datastore.apiTokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val application: Application,
    private val useCases: UseCases,
    @Named(value = MyString.USER) private val _user: MutableStateFlow<User>,
) : ViewModel() {

    private val _inputUser = MutableStateFlow(User())
    val inputUser = _inputUser.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible = _passwordVisible.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object HideKeyBoard : UiEvent()
        object NavigateBack : UiEvent()
        object LogInSuccess : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }

    fun onUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(uiEvent) }
    }

    fun onUsernameChanged(newUsername: String) {
        _inputUser.value = inputUser.value.copy(username = newUsername)
    }

    fun onPasswordChanged(newPassword: String) {
        _inputUser.value = inputUser.value.copy(password = newPassword)
    }

    fun toggleVisiblePassword() {
        _passwordVisible.value = !passwordVisible.value
    }

    fun logIn() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = useCases.logIn(inputUser.value)
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.status == "ok" && item.code == 200) {
                        _user.value = item.data.user
                        ApiToken.token.value = item.data.token
                        CoroutineScope(Dispatchers.Main).launch {
                            application.apiTokenStore.updateData {
                                Token(value = ApiToken.token.value)
                            }
                        }
                        _loading.value = false
                        _eventFlow.emit(UiEvent.LogInSuccess)
                    } else {
                        delay(500)
                        _loading.value = false
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.wrong_username_or_password)))
                    }
                }
            } catch (e: IOException) {
                _loading.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.no_connection)))
                return@launch
            } catch (e: HttpException) {
                _loading.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.no_connection)))
                return@launch
            } catch (e: SocketTimeoutException) {
                _loading.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.no_connection)))
                return@launch
            } catch (e: Exception) {
                _loading.value = false
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.error_can_not_log_in)))
                return@launch
            }
        }
    }
}
