package com.sanryoo.matcher.features.domain.usecase

data class UseCases(
    val signUp: SignUp,
    val logIn: LogIn,
    val getUser: GetUser,
    val updateUser: UpdateUser,
    val changePassword: ChangePassword,
    val postImage: PostImage,
    val getMessages: GetMessages,
    val postImageMessage: PostImageMessage
)
