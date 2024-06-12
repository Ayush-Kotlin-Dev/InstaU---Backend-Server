package instaU.ayush.com.chat.resource.data

@kotlinx.serialization.Serializable
data class Message(
    val messageId : Long,
    val sessionId: Long,
    val textMessage: String,
    val sender: String,
    val receiver: String,
    val timestamp: String
)
