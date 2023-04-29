package com.sanryoo.matcher.features.domain.model.send_and_response_data

data class ResponseObject<T>(
    val code: Int = 200,
    val status: String = "",
    val message: String = "",
    val data: T
)
