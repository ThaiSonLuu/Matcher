package com.sanryoo.matcher.features.data.api

import com.sanryoo.matcher.features.domain.model.*
import com.sanryoo.matcher.features.domain.model.send_and_response_data.LogInResponse
import com.sanryoo.matcher.features.domain.model.send_and_response_data.ResponseObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @Headers("Exclude-Header: true")
    @POST("/api/v1/user/signup")
    suspend fun signUp(@Body user: User): Response<ResponseObject<User>>

    @Headers("Exclude-Header: true")
    @POST("/api/v1/user/login")
    suspend fun logIn(@Body user: User): Response<ResponseObject<LogInResponse>>

    @GET("/api/v1/user/{id}")
    suspend fun getUser(
        @Path("id") id: Long
    ): Response<ResponseObject<User>>

    @PUT("/api/v1/user")
    suspend fun updateUser(
        @Body user: User
    ): Response<ResponseObject<User>>

    @FormUrlEncoded
    @PUT("/api/v1/user/change_password")
    suspend fun changePassword(
        @Field("id") id: Long,
        @Field("oldpassword") oldPassword: String,
        @Field("newpassword") newPassword: String
    ): Response<ResponseObject<User>>

    @Multipart
    @POST("/file/profile")
    suspend fun postImage(
        @Part("id") id: RequestBody,
        @Part("username") username: RequestBody,
        @Part("column") column: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<ResponseObject<User>>

    @Multipart
    @POST("/file/message")
    suspend fun postImageMessage(
        @Part("idsend") idsend: RequestBody,
        @Part("idreceive") idreceive: RequestBody,
        @Part image: MultipartBody.Part
    )

    @GET("/api/v1/message/{idsend}-{idreceive}-{start}-{end}")
    suspend fun getMessages(
        @Path("idsend") idsend: Long,
        @Path("idreceive") idreceive: Long,
        @Path("start") start: Long,
        @Path("end") end: Long
    ): Response<ResponseObject<List<Message>>>

}
