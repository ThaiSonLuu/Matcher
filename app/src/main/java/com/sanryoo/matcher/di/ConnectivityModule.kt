package com.sanryoo.matcher.di

import android.content.Context
import com.sanryoo.matcher.features.domain.connectivity.NetworkConnectivityObserve
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserve(@ApplicationContext context: Context) =
        NetworkConnectivityObserve(context)

}