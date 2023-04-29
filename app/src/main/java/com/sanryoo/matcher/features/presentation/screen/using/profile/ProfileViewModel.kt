package com.sanryoo.matcher.features.presentation.screen.using.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.usecase.UseCases
import com.sanryoo.matcher.features.util.BottomSheet
import com.sanryoo.matcher.features.util.MyString
import dagger.hilt.android.lifecycle.HiltViewModel
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
class ProfileViewModel @Inject constructor(
    private val application: Application,
    private val useCases: UseCases,
    private val _reLogin: MutableSharedFlow<Unit>,
    @Named(value = MyString.USER) private val _user: MutableStateFlow<User>
) : ViewModel() {

    val user = _user.asStateFlow()

    private val _chooseBottomSheet = MutableStateFlow(BottomSheet.DEFAULT)
    val chooseBottomSheet = _chooseBottomSheet.asStateFlow()

    private val _changeName = MutableStateFlow(false)
    val changeName = _changeName.asStateFlow()

    private val _tagIndex = MutableStateFlow(0)
    val tagIndex = _tagIndex.asStateFlow()

    private val _chooseAge = MutableStateFlow(0)
    val chooseAge = _chooseAge.asStateFlow()

    private val _chooseStatus = MutableStateFlow(0)
    val chooseStatus = _chooseStatus.asStateFlow()

    private val _chooseHeight = MutableStateFlow(0)
    val chooseHeight = _chooseHeight.asStateFlow()

    private val _chooseWeight = MutableStateFlow(0)
    val chooseWeight = _chooseWeight.asStateFlow()

    private val _chooseIncome = MutableStateFlow(0)
    val chooseIncome = _chooseIncome.asStateFlow()

    private val _chooseAppearance = MutableStateFlow(0)
    val chooseAppearance = _chooseAppearance.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class BottomSheet(val state: Boolean) : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun tagMyInformation() {
        _tagIndex.value = 0
    }

    fun tagMyRequire() {
        _tagIndex.value = 1
    }

    fun selectedAvatar(uri: Uri) {
        onEvent(ProfileEvent.PostImage("avatar", uri, application))
    }

    fun selectedImage1(uri: Uri) {
        onEvent(ProfileEvent.PostImage("image1", uri, application))
    }

    fun selectedImage2(uri: Uri) {
        onEvent(ProfileEvent.PostImage("image2", uri, application))
    }

    fun selectedImage3(uri: Uri) {
        onEvent(ProfileEvent.PostImage("image3", uri, application))
    }


    fun onFullnameChanged(newFullname: String) {
        if (newFullname.trim().length >= 3 && newFullname.trim().contains(" ")) {
            onEvent(ProfileEvent.UpdateUser(user.value.copy(fullname = newFullname.trim())))
        }
        _changeName.value = false
    }

    fun changeName() {
        _changeName.value = true
    }

    fun onCanCel() {
        _changeName.value = false
    }

    fun showChooseSex() {
        _chooseBottomSheet.value = BottomSheet.SEX
        onUiEvent(UiEvent.BottomSheet(true))
    }

    fun chooseSex(sex: Int) {
        onEvent(ProfileEvent.UpdateUser(user.value.copy(sex = sex)))
        onUiEvent(UiEvent.BottomSheet(false))
    }


    fun showChooseStatus(column: Int) {
        _chooseBottomSheet.value = BottomSheet.STATUS
        _chooseStatus.value = column
        onUiEvent(UiEvent.BottomSheet(true))
    }

    fun chooseStatus(status: Int) {
        when (chooseStatus.value) {
            0 -> onEvent(ProfileEvent.UpdateUser(user.value.copy(status = status)))
            1 -> onEvent(ProfileEvent.UpdateUser(user.value.copy(status1 = status)))
        }
        onUiEvent(UiEvent.BottomSheet(false))
    }

    fun showChooseAge(column: Int) {
        _chooseBottomSheet.value = BottomSheet.AGE
        _chooseAge.value = column
        onUiEvent(UiEvent.BottomSheet(true))
    }

    fun chooseAge(inputAge: Int) {
        when (chooseAge.value) {
            0 -> {
                onEvent(ProfileEvent.UpdateUser(user.value.copy(age = inputAge)))
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BottomSheet(false))
                    delay(500)
                    _chooseBottomSheet.value = BottomSheet.DEFAULT
                }
            }
            1 -> {
                onEvent(ProfileEvent.UpdateUser(user.value.copy(age1 = inputAge)))
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BottomSheet(false))
                    delay(450)
                    _chooseBottomSheet.value = BottomSheet.DEFAULT
                    delay(50)
                    showChooseAge(2)
                }
            }
            2 -> {
                onEvent(ProfileEvent.UpdateUser(user.value.copy(age2 = inputAge)))
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BottomSheet(false))
                    delay(500)
                    _chooseBottomSheet.value = BottomSheet.DEFAULT
                }
            }
        }
    }

    fun showChooseHeight(column: Int) {
        _chooseBottomSheet.value = BottomSheet.HEIGHT
        _chooseHeight.value = column
        onUiEvent(UiEvent.BottomSheet(true))
    }

    fun chooseHeight(inputHeight: Int) {
        when (chooseHeight.value) {
            0 -> {
                onEvent(ProfileEvent.UpdateUser(user.value.copy(height = inputHeight)))
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BottomSheet(false))
                    delay(500)
                    _chooseBottomSheet.value = BottomSheet.DEFAULT
                }
            }
            1 -> {
                onEvent(ProfileEvent.UpdateUser(user.value.copy(height1 = inputHeight)))
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BottomSheet(false))
                    delay(450)
                    _chooseBottomSheet.value = BottomSheet.DEFAULT
                    delay(50)
                    showChooseHeight(2)
                }
            }
            2 -> {
                onEvent(ProfileEvent.UpdateUser(user.value.copy(height2 = inputHeight)))
                viewModelScope.launch {
                    onUiEvent(UiEvent.BottomSheet(false))
                    delay(500)
                    _chooseBottomSheet.value = BottomSheet.DEFAULT
                }
            }
        }
    }

    fun showChooseWeight(column: Int) {
        _chooseBottomSheet.value = BottomSheet.WEIGHT
        _chooseWeight.value = column
        onUiEvent(UiEvent.BottomSheet(true))
    }

    fun chooseWeight(inputWeight: Int) {
        when (chooseWeight.value) {
            0 -> {
                onEvent(ProfileEvent.UpdateUser(user.value.copy(weight = inputWeight)))
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BottomSheet(false))
                    delay(500)
                    _chooseBottomSheet.value = BottomSheet.DEFAULT
                }
            }
            1 -> {
                onEvent(ProfileEvent.UpdateUser(user.value.copy(weight1 = inputWeight)))
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BottomSheet(false))
                    delay(450)
                    _chooseBottomSheet.value = BottomSheet.DEFAULT
                    delay(50)
                    showChooseWeight(2)
                }
            }
            2 -> {
                onEvent(ProfileEvent.UpdateUser(user.value.copy(weight2 = inputWeight)))
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.BottomSheet(false))
                    delay(500)
                    _chooseBottomSheet.value = BottomSheet.DEFAULT
                }
            }
        }
    }


    fun chooseIncome(inputIncome: Int) {
        when (chooseIncome.value) {
            0 -> onEvent(ProfileEvent.UpdateUser(user.value.copy(income = inputIncome)))
            1 -> onEvent(ProfileEvent.UpdateUser(user.value.copy(income1 = inputIncome)))
        }
        onUiEvent(UiEvent.BottomSheet(false))
    }

    fun showChooseIncome(column: Int) {
        _chooseBottomSheet.value = BottomSheet.INCOME
        _chooseIncome.value = column
        onUiEvent(UiEvent.BottomSheet(true))
    }

    fun chooseAppearance(inputAppearance: Double) {
        when (chooseAppearance.value) {
            0 -> onEvent(ProfileEvent.UpdateUser(user.value.copy(appearance = inputAppearance)))
            1 -> onEvent(ProfileEvent.UpdateUser(user.value.copy(appearance1 = inputAppearance)))
        }
        onUiEvent(UiEvent.BottomSheet(false))
    }

    fun showChooseAppearance(column: Int) {
        _chooseBottomSheet.value = BottomSheet.APPEARANCE
        _chooseAppearance.value = column
        onUiEvent(UiEvent.BottomSheet(true))
    }

    fun chooseDistance(require: Int) {
        onEvent(ProfileEvent.UpdateUser(user.value.copy(distance = require)))
        onUiEvent(UiEvent.BottomSheet(false))
    }

    fun showChooseDistance() {
        _chooseBottomSheet.value = BottomSheet.DISTANCE
        onUiEvent(UiEvent.BottomSheet(true))
    }

    fun chooseEasy(easy: Int) {
        _user.value = user.value.copy(easy = easy)
        onEvent(ProfileEvent.UpdateUser(user.value.copy(easy = easy)))
    }

    private fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.PostImage -> {
                viewModelScope.launch {
                    try {
                        val response = useCases.postImage(
                            user.value, event.column, event.uri, event.context
                        )
                        if (response.isSuccessful) {
                            val item = response.body()
                            if (item?.status == "ok" && item.code == 200) {
                                _user.value = item.data
                            } else if (item?.status == "failed" && item.code == 401) { //Token was expired
                                _reLogin.emit(Unit)
                            }
                        }
                    } catch (e: IOException) {
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_change_image_due_to_connection_error)))
                        return@launch
                    } catch (e: HttpException) {
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_change_image_due_to_connection_error)))
                        return@launch
                    } catch (e: SocketTimeoutException) {
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_change_image_due_to_connection_error)))
                        return@launch
                    } catch (e: Exception) {
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_change_image)))
                        return@launch
                    }
                }
            }
            is ProfileEvent.UpdateUser -> {
                viewModelScope.launch {
                    try {
                        val response = useCases.updateUser(event.information)
                        if (response.isSuccessful) {
                            val item = response.body()
                            if (item?.status == "ok" && item.code == 200) {
                                _user.value = item.data
                            } else if (item?.status == "failed" && item.code == 401) { //Token was expired
                                _reLogin.emit(Unit)
                            }
                        }
                    } catch (e: IOException) {
                        _eventFlow.emit(UiEvent.BottomSheet(false))
                        delay(500L)
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_update_information_due_to_connection_error)))
                        return@launch
                    } catch (e: HttpException) {
                        _eventFlow.emit(UiEvent.BottomSheet(false))
                        delay(500L)
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_update_information_due_to_connection_error)))
                        return@launch
                    } catch (e: SocketTimeoutException) {
                        _eventFlow.emit(UiEvent.BottomSheet(false))
                        delay(500L)
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_update_information_due_to_connection_error)))
                        return@launch
                    } catch (e: Exception) {
                        _eventFlow.emit(UiEvent.BottomSheet(false))
                        delay(500L)
                        _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_update_information)))
                        return@launch
                    }
                }
            }
        }
    }
}