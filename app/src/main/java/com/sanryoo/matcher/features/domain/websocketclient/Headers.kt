package com.sanryoo.matcher.features.domain.websocketclient

interface Headers {
    companion object {
        const val VERSION = "accept-version"
        const val HEARTBEAT = "heart-beat"
        const val DESTINATION = "destination"
        const val CONTENT_TYPE = "content-type"
        const val MESSAGE_ID = "message-id"
        const val AUTHORIZATION = "Authorization"
        const val ID = "id"
        const val ACK = "ack"
    }
}