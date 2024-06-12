package instaU.ayush.com.chat.resource

import java.util.*

data class MessageEntity(

    val messageId: Long ,
    val sessionId: Long,
    val textMessage: String,
    val sender: Long,
    val receiver: Long,
    val timestamp:String
)
