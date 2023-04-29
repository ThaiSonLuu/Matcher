package com.sanryoo.matcher.features.presentation.screen.signup

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.domain.model.InvalidUserException
import com.sanryoo.matcher.features.domain.usecase.UseCases
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
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val application: Application,
    private val useCases: UseCases,
    @Named(value = MyString.SIGN_UP) private val _signup: MutableStateFlow<Int>
) : ViewModel() {

    val signup = _signup.asStateFlow()

    private val _inputSignUp = MutableStateFlow(InputSignUpState())
    val inputSignUp = _inputSignUp.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object HideKeyBoard : UiEvent()
        object NavigateBack : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun onUsernameChanged(newUsername: String) {
        _inputSignUp.value = inputSignUp.value.copy(username = newUsername)
    }

    fun onPasswordChanged(newPassword: String) {
        _inputSignUp.value = inputSignUp.value.copy(password = newPassword)
    }

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        _inputSignUp.value = inputSignUp.value.copy(confirmPassword = newConfirmPassword)
    }

    fun togglePasswordVisible() {
        _inputSignUp.value =
            inputSignUp.value.copy(passwordVisible = !inputSignUp.value.passwordVisible)
    }

    fun toggleConfirmPasswordVisible() {
        _inputSignUp.value =
            inputSignUp.value.copy(confirmPasswordVisible = !inputSignUp.value.confirmPasswordVisible)
    }

    fun signUp() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = useCases.signUp(
                    inputSignUp.value.username,
                    inputSignUp.value.password,
                    inputSignUp.value.confirmPassword,
                    application
                )
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.status == "ok" && item.code == 200) {
                        CoroutineScope(Dispatchers.IO).launch {
                            delay(1000)
                            _signup.value = 1
                        }
                        _loading.value = false
                        _eventFlow.emit(UiEvent.NavigateBack)
                    } else {
                        _loading.value = false
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.username_already_exists)))
                    }
                }
            } catch (e: InvalidUserException) {
                _loading.value = false
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        e.message ?: application.getString(R.string.input_error)
                    )
                )
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
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_sign_up)))
                return@launch
            }
        }
    }
}