package instaU.ayush.com.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class SendMessageRequest(
    val receiverId: Long,
    val content: String
)

@Serializable
data class ChatMessage(
    val id: Long = 0,
    val sender: String,
    val recipient: String,
    val message: String,
    val timestamp: Long
)
