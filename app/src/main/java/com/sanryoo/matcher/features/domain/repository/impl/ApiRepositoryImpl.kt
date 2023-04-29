package com.sanryoo.matcher.features.domain.repository.impl

import com.sanryoo.matcher.features.data.api.APIService
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.repository.ApiRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiRepositoryImpl(private val api: APIService) : ApiRepository {

    override suspend fun signUp(user: User) = api.signUp(user)

    override suspend fun logIn(user: User) = api.logIn(user)

    override suspend fun getUser(id: Long) = api.getUser(id)

    override suspend fun updateUser(user: User) = api.updateUser(user)

    override suspend fun changePassword(id: Long, oldPassword: String, newPassword: String) =
        api.changePassword(id, oldPassword, newPassword)

    override suspend fun postImage(
        id: RequestBody,
        username: RequestBody,
        column: RequestBody,
        image: MultipartBody.Part,
    ) = api.postImage(id, username, column, image)

    override suspend fun getMessages(
        idsend: Long, idreceive: Long, start: Long, end: Long
    ) = api.getMessages(idsend, idreceive, start, end)

    override suspend fun postImageMessage(
        idsend: RequestBody,
        idreceive: RequestBody,
        image: MultipartBody.Part
    ) = api.postImageMessage(idsend, idreceive, image)
}