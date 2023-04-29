package com.sanryoo.matcher.features.data.api

import com.sanryoo.matcher.BuildConfig
import com.sanryoo.matcher.features.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class SignUpAPITest {

    private lateinit var api: APIService

    @Before
    fun setUp() {
        api = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }

    @Test
    fun signUpValid() {
        val user = User(username = "abcde12345", password = "Matcher123456789.")
        runTest {
            val response = api.signUp(user)
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

    @Test
    fun usernameAlreadyExist() {
        val user = User(username = "abcde12345", password = "Matcher123456789.")
        runTest {
            val response = api.signUp(user)
            if (response.isSuccessful) {
                val item = response.body()
                if (item?.code == 400 && item.status == "failed") {
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