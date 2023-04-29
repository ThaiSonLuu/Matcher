package com.sanryoo.matcher.features.domain.usecase

import android.content.Context
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.domain.model.InvalidUserException
import com.sanryoo.matcher.features.domain.model.send_and_response_data.ResponseObject
import com.sanryoo.matcher.features.domain.repository.ApiRepository
import com.sanryoo.matcher.features.domain.model.User
import retrofit2.Response

class ChangePassword(private val repository: ApiRepository) {

    @Throws(InvalidUserException::class)
    suspend operator fun invoke(
        id: Long,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
        context: Context
    ): Response<ResponseObject<User>> {
        if (newPassword.length < 16) {
            throw InvalidUserException(context.getString(R.string.password_must_be_longer_than_16_characters))
        } else if (!checkPassword(newPassword)) {
            throw InvalidUserException(context.getString(R.string.password_must_be_include))
        } else if (newPassword != confirmPassword) {
            throw InvalidUserException(context.getString(R.string.confirmation_password_does_not_match))
        }
        return repository.changePassword(id, oldPassword, newPassword)
    }

}