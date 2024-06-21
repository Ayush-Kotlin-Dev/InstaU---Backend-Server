package instau.ayush.com.model.chat

import instau.ayush.com.chat.resource.data.Message


@kotlinx.serialization.Serializable
data class UserEntity(
    val id: Long,
    val username: String? = null,
    val email: String,
    val avatar: String? = null,
    val password: String? = null,
    val lastMessage: Message? = null
)