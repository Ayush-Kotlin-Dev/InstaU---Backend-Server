
package instau.ayush.com.chat.domain.model.firendList

import instau.ayush.com.chat.resource.data.Message

@kotlinx.serialization.Serializable
data class FriendDataResponseDto(
    val friendInfo: FriendInfo? = null
)

@kotlinx.serialization.Serializable
data class FriendInfo(
    val username: String? = null,
    val userId : Long? = null,
    val email: String? = null,
    val avatar: String? = null,
    val lastMessage: Message? = null
)
