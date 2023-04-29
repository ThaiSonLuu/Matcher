package com.sanryoo.matcher.features.presentation.screen.using.change_password

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.domain.model.InvalidUserException
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.usecase.UseCases
import com.sanryoo.matcher.features.util.MyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val application: Application,
    private val useCases: UseCases,
    private val _reLogin: MutableSharedFlow<Unit>,
    @Named(value = MyString.USER) private val _user: MutableStateFlow<User>,
) : ViewModel() {

    private val _inputState = MutableStateFlow(InputChangePasswordState())
    val inputState = _inputState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object NavigateBack : UiEvent()
        class ShowSnackBar(val message: String) : UiEvent()
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun onOldPasswordChanged(s: String) {
        _inputState.value = inputState.value.copy(oldPassword = s)
    }

    fun onNewPasswordChanged(s: String) {
        _inputState.value = inputState.value.copy(newPassword = s)
    }

    fun onConfirmPasswordChanged(s: String) {
        _inputState.value = inputState.value.copy(confirmPassword = s)
    }

    fun toggleOldPasswordVisible() {
        _inputState.value =
            inputState.value.copy(oldPasswordVisible = !inputState.value.oldPasswordVisible)
    }

    fun toggleNewPasswordVisible() {
        _inputState.value =
            inputState.value.copy(newPasswordVisible = !inputState.value.newPasswordVisible)
    }

    fun toggleConfirmPasswordVisible() {
        _inputState.value =
            inputState.value.copy(confirmPasswordVisible = !inputState.value.confirmPasswordVisible)
    }

    fun changePassword() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = useCases.changePassword(
                    _user.value.id,
                    inputState.value.oldPassword,
                    inputState.value.newPassword,
                    inputState.value.confirmPassword,
                    application
                )
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.status == "ok" && item.code == 200) {
                        _user.value.password = item.data.password
                        _loading.value = false
                        _eventFlow.emit(UiEvent.NavigateBack)
                    } else if (item?.status == "failed" && item.code == 401) { //Token was expired
                        _reLogin.emit(Unit)
                    } else {
                        _loading.value = false
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                item?.message ?: application.getString(
                                    R.string.could_not_change_password
                                )
                            )
                        )
                    }
                }
            } catch (e: InvalidUserException) {
                _loading.value = false
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        e.message ?: application.getString(R.string.could_not_change_password)
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
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.could_not_change_password)))
                return@launch
            }
        }
    }

}