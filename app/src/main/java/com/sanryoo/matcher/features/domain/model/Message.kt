package com.sanryoo.matcher.features.domain.model

import com.sanryoo.matcher.features.util.MessageType
import java.util.Date

data class Message(
    var messageid: Long = 0,
    var usersend: User = User(),
    var userreceive: User = User(),
    var date: Date? = null,
    var type: Int = MessageType.TEXT,
    var data: String = "",
    var width: Int = 0,
    var height: Int = 0
)