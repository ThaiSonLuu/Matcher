package com.sanryoo.matcher.features.domain.usecase

import com.sanryoo.matcher.features.domain.repository.ApiRepository

class GetMessages(private val repository: ApiRepository) {

    suspend operator fun invoke(idsend: Long, idreceive: Long, start: Long, end: Long) =
        repository.getMessages(idsend, idreceive, start, end)

}