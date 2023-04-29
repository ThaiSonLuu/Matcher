package com.sanryoo.matcher.features.domain.websocketclient

import java.util.regex.Pattern

data class Message(
    var command: String? = null,
    var headers: Map<String, String> = HashMap(),
    var payload: String? = null
) {
    companion object {
        const val TERMINATE_MESSAGE_SYMBOL = "\u0000"
        val PATTERN_HEADER: Pattern = Pattern.compile("([^:\\s]+)\\s*:\\s*([^:\\s]+)")
    }
}
