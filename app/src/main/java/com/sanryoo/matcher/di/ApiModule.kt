package com.sanryoo.matcher.di

import com.sanryoo.matcher.BuildConfig
import com.sanryoo.matcher.features.data.api.APIService
import com.sanryoo.matcher.features.data.api.AuthInterceptor
import com.sanryoo.matcher.features.domain.repository.ApiRepository
import com.sanryoo.matcher.features.domain.repository.impl.ApiRepositoryImpl
import com.sanryoo.matcher.features.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIService(client: OkHttpClient): APIService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: APIService): ApiRepository {
        return ApiRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideUseCases(repository: ApiRepository): UseCases {
        return UseCases(
            SignUp(repository),
            LogIn(repository),
            GetUser(repository),
            UpdateUser(repository),
            ChangePassword(repository),
            PostImage(repository),
            GetMessages(repository),
            PostImageMessage(repository)
        )
    }
}