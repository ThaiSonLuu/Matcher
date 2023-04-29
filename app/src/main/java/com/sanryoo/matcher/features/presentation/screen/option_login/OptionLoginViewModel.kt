package com.sanryoo.matcher.features.presentation.screen.option_login

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.data.api.ApiToken
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.usecase.UseCases
import com.sanryoo.matcher.features.util.MyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OptionLoginViewModel @Inject constructor(
    private val application: Application,
    private val useCases: UseCases,
    val signInClient: GoogleSignInClient,
    _visibleSplashScreen: MutableStateFlow<Boolean>,
    @Named(value = MyString.USER) private val _user: MutableStateFlow<User>
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        object LogInSuccess : UiEvent()
        data class Navigate(val route: String) : UiEvent()
        data class ShowSnackBar(val message: String) : UiEvent()
    }

    init {
        _visibleSplashScreen.value = false
    }

    fun onUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch { _eventFlow.emit(uiEvent) }
    }

    fun logInWithGoogle(account: GoogleSignInAccount) {
        val googleAvatar = account.photoUrl?.toString() ?: ""
        val googleName = account.displayName ?: ""
        viewModelScope.launch {
            try {
                val response = useCases.logIn(
                    User(
                        username = "google_${account.id}",
                        idgoogle = account.id ?: "default_id_google"
                    )
                )
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.status == "ok") {
                        if (item.code == 201) {
                            _user.value = item.data.user.copy(
                                avatar = googleAvatar, fullname = googleName
                            )
                            ApiToken.token.value = item.data.token
                            CoroutineScope(Dispatchers.Main).launch {
                                useCases.updateUser(_user.value)
                            }
                            _eventFlow.emit(UiEvent.LogInSuccess)
                        } else if (item.code == 200) {
                            _user.value = item.data.user
                            ApiToken.token.value = item.data.token
                            _eventFlow.emit(UiEvent.LogInSuccess)
                        }
                    }
                }
            } catch (e: IOException) {
                signInClient.signOut()
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.no_connection)))
                return@launch
            } catch (e: HttpException) {
                signInClient.signOut()
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.no_connection)))
                return@launch
            } catch (e: SocketTimeoutException) {
                signInClient.signOut()
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.no_connection)))
                return@launch
            } catch (e: Exception) {
                signInClient.signOut()
                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_log_in_with_google)))
                return@launch
            }
        }
    }

    fun logInWithFacebook(context: Context) {
        val callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            callbackManager = callbackManager,
            callback = object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.can_not_log_in_with_facebook)))
                }

                override fun onError(error: FacebookException) {
                    onUiEvent(UiEvent.ShowSnackBar(application.getString(R.string.can_not_log_in_with_facebook)))
                }

                override fun onSuccess(result: LoginResult) {
                    val accessToken = result.accessToken
                    val request = GraphRequest.newMeRequest(accessToken) { obj, _ ->
                        val facebookAvatar =
                            obj?.getJSONObject("picture")?.getJSONObject("data")?.getString("url")
                                ?: ""
                        val facebookName = obj?.getString("name") ?: ""
                        viewModelScope.launch {
                            try {
                                val response = useCases.logIn(
                                    User(
                                        username = "facebook_${obj?.getString("id")}",
                                        idfacebook = obj?.getString("id") ?: "default_id_facebook"
                                    )
                                )
                                if (response.isSuccessful) {
                                    val item = response.body()
                                    if (item?.status == "ok") {
                                        if (item.code == 201) {
                                            _user.value = item.data.user.copy(
                                                avatar = facebookAvatar,
                                                fullname = facebookName
                                            )
                                            ApiToken.token.value = item.data.token
                                            CoroutineScope(Dispatchers.Main).launch {
                                                useCases.updateUser(_user.value)
                                            }
                                            _eventFlow.emit(UiEvent.LogInSuccess)
                                        } else if (item.code == 200) {
                                            _user.value = item.data.user
                                            ApiToken.token.value = item.data.token
                                            _eventFlow.emit(UiEvent.LogInSuccess)
                                        }
                                    }
                                }
                            } catch (e: IOException) {
                                LoginManager.getInstance().logOut()
                                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.no_connection)))
                                return@launch
                            } catch (e: HttpException) {
                                LoginManager.getInstance().logOut()
                                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.no_connection)))
                                return@launch
                            } catch (e: SocketTimeoutException) {
                                LoginManager.getInstance().logOut()
                                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.no_connection)))
                                return@launch
                            } catch (e: Exception) {
                                LoginManager.getInstance().logOut()
                                _eventFlow.emit(UiEvent.ShowSnackBar(application.getString(R.string.can_not_log_in_with_facebook)))
                                return@launch
                            }
                        }
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,link,picture.type(large)")
                    request.parameters = parameters
                    request.executeAsync()
                }
            }
        )
        LoginManager.getInstance().logInWithReadPermissions(
            permissions = listOf("public_profile"),
            activityResultRegistryOwner = context as ActivityResultRegistryOwner,
            callbackManager = callbackManager,
        )
    }

}