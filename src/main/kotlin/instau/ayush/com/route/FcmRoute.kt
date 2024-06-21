package instau.ayush.com.route


import com.google.firebase.messaging.FirebaseMessaging
import instau.ayush.com.model.SendMessageDto
import instau.ayush.com.model.toMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.SendMessageRoute() {

    route(path = "/send") {
        post {
            val body = call.receiveNullable<SendMessageDto>() ?: return@post call.respondText("Invalid request")

            FirebaseMessaging.getInstance().send(body.toMessage())
            call.respond(HttpStatusCode.OK, "Message sent")
        }


    }
    route(path = "/broadcast"){
        post {
            val body = call.receiveNullable<SendMessageDto>() ?: return@post call.respondText("Invalid request")

            FirebaseMessaging.getInstance().send(body.toMessage())
            call.respond(HttpStatusCode.OK, "Message sent")
        }
    }
}

