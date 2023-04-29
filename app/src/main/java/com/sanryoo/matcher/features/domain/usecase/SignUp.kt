package com.sanryoo.matcher.features.domain.usecase

import android.content.Context
import com.sanryoo.matcher.R
import com.sanryoo.matcher.features.domain.model.InvalidUserException
import com.sanryoo.matcher.features.domain.model.send_and_response_data.ResponseObject
import com.sanryoo.matcher.features.domain.repository.ApiRepository
import com.sanryoo.matcher.features.domain.model.User
import retrofit2.Response

class SignUp(private val repository: ApiRepository) {

    @Throws(InvalidUserException::class)
    suspend operator fun invoke(
        username: String,
        password: String,
        confirmPassword: String,
        context: Context
    ): Response<ResponseObject<User>> {
        if (!isValidUsername(username)) {
            throw InvalidUserException(context.getString(R.string.username_contains_only_lowercase_letters_and_numbers))
        } else if (username.length < 8) {
            throw InvalidUserException(context.getString(R.string.username_must_be_longer_than_8_characters))
        } else if (!checkPassword(password)) {
            throw InvalidUserException(context.getString(R.string.password_must_be_include))
        } else if (password.length < 16) {
            throw InvalidUserException(context.getString(R.string.password_must_be_longer_than_16_characters))
        } else if (password != confirmPassword) {
            throw InvalidUserException(context.getString(R.string.confirmation_password_does_not_match))
        }
        return repository.signUp(User(username = username, password = password))
    }
}

fun isValidUsername(string: String): Boolean {
    if (!string[0].isLowerCase()) {
        return false
    }
    string.forEach { c ->
        if (!(c.isLowerCase() || (c in '0'..'9')))
            return false
    }
    return true
}

fun checkPassword(string: String): Boolean {
    var countNumber = 0
    var countLowerCase = 0
    var countUpperCase = 0
    var countSpecialLetters = 0

    string.forEach { c ->
        when (c) {
            in '0'..'9' -> countNumber++
            in 'a'..'z' -> countLowerCase++
            in 'A'..'Z' -> countUpperCase++
            '.', '_' -> countSpecialLetters++
        }
    }
    return countNumber * countLowerCase * countUpperCase * countSpecialLetters != 0
}