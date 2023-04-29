package com.sanryoo.matcher.features.presentation.screen.using.message

import com.sanryoo.matcher.features.domain.model.Message

data class MessageState(
    var inputMessage: String = "",
    var listMessage: List<Message> = emptyList()
)