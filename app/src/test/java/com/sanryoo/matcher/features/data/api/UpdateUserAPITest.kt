package com.sanryoo.matcher.features.data.api

import com.sanryoo.matcher.BuildConfig
import com.sanryoo.matcher.features.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class UpdateUserAPITest {

    private lateinit var api: APIService

    private lateinit var user: User

    @Before
    fun setUp() {
        api = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
        val user1 = User(username = "abcde12345", password = "Matcher123456789.")
        runTest {
            val response = api.logIn(user1)
            if (response.isSuccessful) {
                val item = response.body()
                if (item?.code == 200 && item.status == "ok") {
                    user = item.data.user
                    ApiToken.token.value = item.data.token
                } else {
                    assert(false)
                }
            } else {
                assert(false)
            }
        }
    }

    @Test
    fun updateUserValid() {
        runTest {
            val response = api.updateUser(user.copy(fullname = "matcher_test_user"))
            if (response.isSuccessful) {
                val item = response.body()
                if (item?.code == 200 && item.status == "ok") {
                    assert(true)
                } else {
                    assert(false)
                }
            } else {
                assert(false)
            }
        }
    }

}