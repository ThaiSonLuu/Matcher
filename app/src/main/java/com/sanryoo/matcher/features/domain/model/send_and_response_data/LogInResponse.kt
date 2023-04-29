package com.sanryoo.matcher.features.domain.model.send_and_response_data

import com.sanryoo.matcher.features.domain.model.User

data class LogInResponse(
    var user: User = User(),
    var token: String = ""
)
