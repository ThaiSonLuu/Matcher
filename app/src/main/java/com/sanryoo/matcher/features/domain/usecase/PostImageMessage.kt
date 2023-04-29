package com.sanryoo.matcher.features.domain.usecase

import android.content.Context
import android.net.Uri
import com.sanryoo.matcher.features.domain.repository.ApiRepository
import com.sanryoo.matcher.features.util.uriToPart
import okhttp3.RequestBody.Companion.toRequestBody

class PostImageMessage(private val repository: ApiRepository) {

    suspend operator fun invoke(idsend: Long, idreceive: Long, uri: Uri, context: Context) {
        repository.postImageMessage(
            idsend.toString().toRequestBody(),
            idreceive.toString().toRequestBody(),
            uriToPart(context, uri)
        )
    }

}