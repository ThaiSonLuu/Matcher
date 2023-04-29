package com.sanryoo.matcher.features.domain.usecase

import com.sanryoo.matcher.features.domain.repository.ApiRepository

class GetUser(private val repository: ApiRepository) {

    suspend operator fun invoke(id: Long) = repository.getUser(id)

}