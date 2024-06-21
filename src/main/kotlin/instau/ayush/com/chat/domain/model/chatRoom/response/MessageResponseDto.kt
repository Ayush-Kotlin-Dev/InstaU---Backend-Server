package instau.ayush.com.chat.domain.model.chatRoom.response
@kotlinx.serialization.Serializable
data class MessageResponseDto(
    val textMessage: String,
    val sender: Long,
    val receiver: Long,
    val timestamp: String
)
