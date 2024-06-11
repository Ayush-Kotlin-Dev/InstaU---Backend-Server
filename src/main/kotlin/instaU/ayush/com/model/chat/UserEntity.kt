package instaU.ayush.com.model.chat



@kotlinx.serialization.Serializable
data class UserEntity(
    val id: String,
    val token: String? = null,
    val username: String? = null,
    val email: String? = null,
    val avatar: String? = null,
    val password: String? = null,
    val lastMessage: Message? = null
)