package instau.ayush.com.plugins

import instau.ayush.com.chat.resource.chatHistoryEndpoint
import instau.ayush.com.chat.resource.friendsListEndpoint
import instau.ayush.com.route.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRouting()
        followsRouting()
        postRouting()
        ProfileRouting()
        postCommentsRouting()
        postLikesRouting()
        SendMessageRoute()
        QnaRouting()
        route("/") {
            get {
                call.respondText("Hello Users this is Ayush!")
            }
        }
        friendsListEndpoint()
        chatHistoryEndpoint()
        EventRouting()
    }
}
