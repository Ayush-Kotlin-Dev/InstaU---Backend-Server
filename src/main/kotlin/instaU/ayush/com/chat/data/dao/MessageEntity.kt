package instaU.ayush.com.chat.resource

data class MessageEntity(
    val messageId: String ,
    val sessionId: String,
    val textMessage: String,
    val sender: String,
    val receiver: String,
    val timestamp: Long
)
