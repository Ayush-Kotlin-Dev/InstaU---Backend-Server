
package instau.ayush.com.chat.resource

import instau.ayush.com.chat.domain.model.chatRoom.response.MessageResponseDto


@kotlinx.serialization.Serializable
data class ChatRoomHistoryState(
    val data: List<MessageResponseDto>? = null,
    val error: HashMap<String, String>? = null
)
