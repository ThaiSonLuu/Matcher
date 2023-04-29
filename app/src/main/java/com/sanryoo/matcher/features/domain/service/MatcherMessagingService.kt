package com.sanryoo.matcher.features.domain.service

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.messaging.FirebaseMessagingService
import com.sanryoo.matcher.features.domain.datastore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MatcherMessagingService : FirebaseMessagingService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    companion object {
        private val fcmTokenKey = stringPreferencesKey("fcm_token")
    }

    override fun onNewToken(token: String) {
        serviceScope.launch {
            this@MatcherMessagingService.dataStore.edit { set ->
                set[fcmTokenKey] = token
            }
        }
        super.onNewToken(token)
    }
}