package com.sanryoo.matcher.features.domain.websocketclient

interface Commands {
    companion object {
        const val CONNECT = "CONNECT"
        const val CONNECTED = "CONNECTED"
        const val SEND = "SEND"
        const val MESSAGE = "MESSAGE"
        const val SUBSCRIBE = "SUBSCRIBE"
        const val UNSUBSCRIBE = "UNSUBSCRIBE"
        const val UNKNOWN = "UNKNOWN"
    }
}