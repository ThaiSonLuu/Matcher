package com.sanryoo.matcher.features.presentation

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sanryoo.matcher.features.data.api.ApiToken
import com.sanryoo.matcher.features.domain.datastore.apiTokenStore
import com.sanryoo.matcher.features.domain.datastore.dataStore
import com.sanryoo.matcher.features.domain.model.Token
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.usecase.UseCases
import com.sanryoo.matcher.features.presentation.screen.using.message.MessageState
import com.sanryoo.matcher.features.util.MyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MatcherViewModal @Inject constructor(
    private val application: Application,
    private val useCases: UseCases,
    private val _messageState: MutableStateFlow<MessageState>,
    _visibleSplashScreen: MutableStateFlow<Boolean>,
    @Named(value = MyString.USER) private val _user: MutableStateFlow<User>,
    @Named(value = MyString.OTHERS) private val _others: MutableStateFlow<User>,
    @Named(value = MyString.SIGN_UP) private val _signup: MutableStateFlow<Int>
) : ViewModel() {

    val user = _user.asStateFlow()
    val visibleSplashScreen = _visibleSplashScreen.asStateFlow()

    companion object {
        private val signupKey = intPreferencesKey("signup")

        private val userKey = stringPreferencesKey("user")
        private val othersKey = stringPreferencesKey("others")
        private val messageStateKey = stringPreferencesKey("message_state")

        private val gson = Gson()
    }

    init {
        if (user.value.id > 0) {
            getData()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            ApiToken.token.value = application.apiTokenStore.data.first().value
            val get = application.dataStore.data.first()
            _signup.value = get[signupKey] ?: 0
            try {
                _user.value = gson.fromJson(get[userKey], User::class.java)
                _others.value = gson.fromJson(get[othersKey], User::class.java)
                _messageState.value = gson.fromJson(get[messageStateKey], MessageState::class.java)

                if (_user.value.id > 0) {
                    getInformation()
                }
            } catch (_: Exception) {
            }
        }
    }

    fun saveData() {
        viewModelScope.launch {
            application.getSharedPreferences("matcher_sf", Context.MODE_PRIVATE).edit().apply {
                putLong("id", user.value.id)
                apply()
            }
            application.apiTokenStore.updateData {
                Token(value = ApiToken.token.value)
            }
            application.dataStore.edit { set ->
                set[signupKey] = _signup.value
                set[userKey] = gson.toJson(user.value)
                set[othersKey] = gson.toJson(_others.value)
                set[messageStateKey] = gson.toJson(
                    _messageState.value.copy(
                        listMessage = if (_messageState.value.listMessage.size <= 60) {
                            _messageState.value.listMessage
                        } else {
                            _messageState.value.listMessage.subList(0, 59)
                        }
                    )
                )
            }
        }
    }

    private fun getInformation() {
        viewModelScope.launch {
            try {
                val response = useCases.getUser(_user.value.id)
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item?.status == "ok" && item.code == 200) {
                        _user.value = item.data
                        if (_user.value.matched <= 0) {
                            _others.value = User()
                        }
                    }
                }
            } catch (e: Exception) {
                return@launch
            }
        }
    }
}