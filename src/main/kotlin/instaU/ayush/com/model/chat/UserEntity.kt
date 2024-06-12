package instaU.ayush.com.model.chat

import instaU.ayush.com.chat.resource.data.Message


@kotlinx.serialization.Serializable
data class UserEntity(
    val id: Long,
    val username: String? = null,
    val email: String? = null,
    val avatar: String? = null,
    val password: String? = null,
    val lastMessage: Message? = null
)