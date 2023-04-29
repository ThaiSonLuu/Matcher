package com.sanryoo.matcher.features.domain.usecase

import com.sanryoo.matcher.features.domain.model.User
import com.sanryoo.matcher.features.domain.repository.ApiRepository

class UpdateUser(private val repository: ApiRepository) {

    suspend operator fun invoke(user: User) = repository.updateUser(user)

}