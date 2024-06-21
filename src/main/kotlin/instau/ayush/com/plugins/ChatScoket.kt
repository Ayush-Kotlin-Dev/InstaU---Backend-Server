package instau.ayush.com.plugins

import instau.ayush.com.chat.resource.chatConnectEndpoint
import instau.ayush.com.route.ChangeInPost
import instau.ayush.com.route.chatRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }


    routing {
        chatRouting()
        chatConnectEndpoint()
        ChangeInPost()

    }
}
