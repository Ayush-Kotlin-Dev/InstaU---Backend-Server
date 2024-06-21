package instau.ayush.com.route


import com.google.firebase.messaging.FirebaseMessaging
import instau.ayush.com.model.*
import instau.ayush.com.repository.fcm.FcmRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

fun Route.SendMessageRoute() {
    val fcmRepository by inject<FcmRepository>()

    route("/fcm/store") {
        post("/store") {
            val request = call.receive<StoreFcmTokenRequest>()

            try {
                fcmRepository.saveToken(request.userId, request.token)
                call.respond(HttpStatusCode.OK, "FCM token saved successfully")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to save FCM token: ${e.message}")
            }

        }
    }


    route(path = "/send") {
        post {
            val body = call.receiveNullable<SendMessageDto>() ?: return@post call.respondText("Invalid request")

            FirebaseMessaging.getInstance().send(body.toMessage())
            call.respond(HttpStatusCode.OK, "Message sent")
        }


    }
    route(path = "/broadcast") {
        post {
            val body = call.receiveNullable<SendMessageDto>() ?: return@post call.respondText("Invalid request")

            FirebaseMessaging.getInstance().send(body.toMessage())
            call.respond(HttpStatusCode.OK, "Message sent")
        }
    }
}


