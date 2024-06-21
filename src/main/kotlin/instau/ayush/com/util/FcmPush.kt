package instau.ayush.com.util

import instau.ayush.com.repository.fcm.FcmRepository
import com.google.firebase.messaging.FirebaseMessaging
import instau.ayush.com.model.NotificationBody
import instau.ayush.com.model.SendMessageDto
import instau.ayush.com.model.toMessage
import com.google.firebase.messaging.Message as FcmMessage

class NotificationService(private val fcmTokenRepository: FcmRepository) {

    suspend fun sendNotificationToReceiver(receiverId: Long, message: String) {
        val receiverToken = fcmTokenRepository.getToken(receiverId)

        val body = SendMessageDto(
            to = receiverToken,
            notification = NotificationBody(
                title = "FireBase Notification",
                body = message
            )
        )
        FirebaseMessaging.getInstance().send(body.toMessage())
    }
}

