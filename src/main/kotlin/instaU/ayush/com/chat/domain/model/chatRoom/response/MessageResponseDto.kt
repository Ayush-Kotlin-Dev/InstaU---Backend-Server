package instaU.ayush.com.chat.domain.model.chatRoom.response
@kotlinx.serialization.Serializable
data class MessageResponseDto(
    val textMessage: String,
    val sender: String,
    val receiver: String,
    val timestamp: Long
)
