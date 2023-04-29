package com.sanryoo.matcher.features.presentation.screen.using.change_password

data class InputChangePasswordState(
    var oldPassword: String = "",
    var newPassword: String = "",
    var confirmPassword: String = "",
    var oldPasswordVisible: Boolean = false,
    var newPasswordVisible: Boolean = false,
    var confirmPasswordVisible: Boolean = false
)