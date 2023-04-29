package com.sanryoo.matcher.di

import com.sanryoo.matcher.BuildConfig
import com.sanryoo.matcher.features.domain.websocketclient.WebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @Singleton
    fun provideStompClient() = WebSocketClient(BuildConfig.WS_URL)

}