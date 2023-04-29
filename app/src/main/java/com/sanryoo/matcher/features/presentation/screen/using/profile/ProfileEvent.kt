package com.sanryoo.matcher.features.presentation.screen.using.profile

import android.content.Context
import android.net.Uri
import com.sanryoo.matcher.features.domain.model.User

sealed class ProfileEvent {
    data class PostImage(val column: String, val uri: Uri, val context: Context) : ProfileEvent()

    data class UpdateUser(val information: User) : ProfileEvent()
}