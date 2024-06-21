package instau.ayush.com.model.chat

import instau.ayush.com.chat.resource.data.Message

@kotlinx.serialization.Serializable
data class User(
    val user: UserData? = null
)

@kotlinx.serialization.Serializable
data class UserData(
    val username: String? = null,
    val userId : Long? = null,
    val email: String? = null,
    val avatar: String? = null,
    val lastMessage: Message? = null
)
