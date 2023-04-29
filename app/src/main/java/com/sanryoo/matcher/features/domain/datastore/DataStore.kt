package com.sanryoo.matcher.features.domain.datastore

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import com.sanryoo.matcher.features.domain.datastore.key_store.CryptoManager
import com.sanryoo.matcher.features.domain.datastore.serializer.TokenSerializer

val Context.dataStore by preferencesDataStore("data_store")
val Context.apiTokenStore by dataStore("api_token.json", TokenSerializer(CryptoManager()))