package com.sanryoo.matcher.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.sanryoo.matcher.features.domain.location.LocationClient
//import com.sanryoo.matcher.features.data.location.LocationClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideLocationClient(@ApplicationContext context: Context) = LocationClient(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )

}