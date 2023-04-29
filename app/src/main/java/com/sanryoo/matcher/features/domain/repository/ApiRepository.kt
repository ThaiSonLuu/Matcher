package com.sanryoo.matcher.features.domain.repository

import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.model.Message
import com.sanryoo.matcher.features.domain.model.send_and_response_data.LogInResponse
import com.sanryoo.matcher.features.domain.model.send_and_response_data.ResponseObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiRepository {

    suspend fun signUp(user: User): Response<ResponseObject<User>>

    suspend fun logIn(user: User): Response<ResponseObject<LogInResponse>>

    suspend fun getUser(id: Long): Response<ResponseObject<User>>

    suspend fun updateUser(user: User): Response<ResponseObject<User>>

    suspend fun changePassword(
        id: Long, oldPassword: String, newPassword: String
    ): Response<ResponseObject<User>>

    suspend fun postImage(
        id: RequestBody, username: RequestBody, column: RequestBody, image: MultipartBody.Part
    ): Response<ResponseObject<User>>

    suspend fun getMessages(
        idsend: Long, idreceive: Long, start: Long, end: Long
    ): Response<ResponseObject<List<Message>>>

    suspend fun postImageMessage(idsend: RequestBody, idreceive: RequestBody, image: MultipartBody.Part)
}