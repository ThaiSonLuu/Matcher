package com.sanryoo.matcher.features.domain.usecase

import android.content.Context
import android.net.Uri
import com.sanryoo.matcher.features.domain.repository.ApiRepository
import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.model.send_and_response_data.ResponseObject
import com.sanryoo.matcher.features.util.uriToPart
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class PostImage(private val repository: ApiRepository) {

    suspend operator fun invoke(
        user: User,
        column: String,
        uri: Uri,
        context: Context
    ): Response<ResponseObject<User>> {
        val id = user.id.toString().toRequestBody()
        val username = user.username.toRequestBody()
        val column1 = column.toRequestBody()
        return repository.postImage(id, username, column1, uriToPart(context, uri))
    }
}