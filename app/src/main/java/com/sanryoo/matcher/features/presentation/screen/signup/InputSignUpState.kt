package com.sanryoo.matcher.features.presentation.screen.signup

data class InputSignUpState (
    var username: String = "",
    var password: String = "",
    var confirmPassword: String = "",
    var passwordVisible: Boolean = false,
    var confirmPasswordVisible: Boolean = false
)