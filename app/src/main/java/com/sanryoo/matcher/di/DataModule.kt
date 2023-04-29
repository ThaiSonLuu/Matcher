package com.sanryoo.matcher.di

import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.presentation.screen.using.message.MessageState
import com.sanryoo.matcher.features.util.MyString
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    @Named(value = MyString.USER)
    fun provideUser() = MutableStateFlow(User())

    @Provides
    @Singleton
    @Named(value = MyString.OTHERS)
    fun provideOthers() = MutableStateFlow(User())

    @Provides
    @Singleton
    @Named(value = MyString.SIGN_UP)
    fun provideSignup() = MutableStateFlow(0)

    @Provides
    @Singleton
    @Named(value = MyString.SENDING_MESSAGE)
    fun provideSendingMessage() = MutableStateFlow(0)

    @Provides
    @Singleton
    fun provideReLogin() = MutableSharedFlow<Unit>()

    @Provides
    @Singleton
    fun provideVisibleSplashScreen() = MutableStateFlow(true)

    @Provides
    @Singleton
    fun provideMessageState() = MutableStateFlow(MessageState())

}