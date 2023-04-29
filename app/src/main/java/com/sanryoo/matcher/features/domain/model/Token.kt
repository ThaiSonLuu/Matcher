package com.sanryoo.matcher.features.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    var value: String = ""
)