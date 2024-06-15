package instaU.ayush.com.plugins

import instaU.ayush.com.chat.resource.chatHistoryEndpoint
import instaU.ayush.com.chat.resource.friendsListEndpoint
import instaU.ayush.com.route.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
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
        static {
            resources("static")
        }
        route("/") {
            get {
                call.respondText("Hello Users this is Ayush!")
            }
        }
            friendsListEndpoint()
            chatHistoryEndpoint()
    }
}
