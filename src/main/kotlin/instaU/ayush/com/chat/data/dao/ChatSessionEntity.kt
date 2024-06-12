package instaU.ayush.com.chat.data.dao

data class ChatSessionEntity(
    val id: String ,
    val sender: Long,
    val receiver: Long,
    val sessionId: Long
)
