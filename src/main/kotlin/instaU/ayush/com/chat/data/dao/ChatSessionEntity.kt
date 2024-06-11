package instaU.ayush.com.chat.data.dao

data class ChatSessionEntity(
    val id: String ,
    val sender: String,
    val receiver: String,
    val sessionId: String
)
