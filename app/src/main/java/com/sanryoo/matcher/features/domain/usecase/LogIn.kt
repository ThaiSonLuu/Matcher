package com.sanryoo.matcher.features.domain.usecase

import com.sanryoo.matcher.features.domain.repository.ApiRepository
import com.sanryoo.matcher.features.domain.model.User

class LogIn(private val repository: ApiRepository) {

    suspend operator fun invoke(user: User) = repository.logIn(user)

}