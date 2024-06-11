package instaU.ayush.com.plugins

import instaU.ayush.com.route.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.inassar.demos.features.chat.resource.chatConnectEndpoint
import me.inassar.demos.features.chat.resource.chatHistoryEndpoint
import me.inassar.demos.features.chat.resource.friendsListEndpoint

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
        authenticate {
            chatConnectEndpoint()
            friendsListEndpoint()
            chatHistoryEndpoint()
        }

    }
}
