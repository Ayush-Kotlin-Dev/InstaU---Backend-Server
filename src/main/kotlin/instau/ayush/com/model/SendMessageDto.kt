package instau.ayush.com.model
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageDto(
    val to: String?,
    val notification: NotificationBody,
)
@Serializable
data class NotificationBody(
    val title: String,
    val body: String,
)
fun SendMessageDto.toMessage(): Message {
    return Message.builder()
//        .putData("title", notification.title)
//        .putData("body", notification.body)
        .setNotification(Notification.builder()
            .setTitle(notification.title)
            .setBody(notification.body)
            .build()
        )

        .apply {
            when {
                to.isNullOrEmpty() -> setTopic("chat")
                else -> setToken(to)
            }
        }
        .build()
}

@Serializable
data  class StoreFcmTokenRequest(
    val token: String,
    val userId: Long
)