package instaU.ayush.com.plugins

import instaU.ayush.com.chat.data.dao.ChatSessionEntity
import instaU.ayush.com.chat.domain.repository.ChatRepository
import instaU.ayush.com.util.IdGenerator
import instaU.ayush.com.util.getLongParameter
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import org.koin.java.KoinJavaComponent.inject
fun Application.configureSession() {

    install(Sessions) {
        cookie<ChatSessionEntity>("MY_SESSION")
    }

    intercept(ApplicationCallPipeline.Plugins) {

        val chatRepository by inject<ChatRepository>(ChatRepository::class.java)

        // Check if the request path is chat/connect'
        if (call.request.path() == "/chat/connect") {
            if (call.sessions.get<ChatSessionEntity>() == null) {
                val sender = call.getLongParameter("sender", true)
                val receiver = call.getLongParameter("receiver", true)

                var sessionId = chatRepository.checkSessionAvailability(sender, receiver)

                if (sessionId == null)
                    sessionId = chatRepository.createNewSession(sender, receiver)

                call.sessions.set(
                    ChatSessionEntity(
                        sender = sender, receiver = receiver, sessionId = sessionId , id = IdGenerator.generateId()
                    )
                )
            }
        }
    }
}