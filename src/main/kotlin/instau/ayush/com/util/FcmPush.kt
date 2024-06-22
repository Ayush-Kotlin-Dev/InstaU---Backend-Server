package instau.ayush.com.util

import instau.ayush.com.repository.fcm.FcmRepository
import com.google.firebase.messaging.FirebaseMessaging
import instau.ayush.com.model.NotificationBody
import instau.ayush.com.model.SendMessageDto
import instau.ayush.com.model.toMessage
import com.google.firebase.messaging.Message as FcmMessage

class NotificationService(private val fcmTokenRepository: FcmRepository) {

    suspend fun sendNotificationToReceiver(receiverId: Long, message: String) {
        val receiverData = fcmTokenRepository.getToken(receiverId)

        if (receiverData != null) {
            val (userName, receiverToken) = receiverData

            val body = SendMessageDto(
                to = receiverToken,
                notification = NotificationBody(
                    title = userName,
                    body = message
                )
            )
            FirebaseMessaging.getInstance().send(body.toMessage())
        } else {
            println("Token not found for user: $receiverId")
        }
    }
}

