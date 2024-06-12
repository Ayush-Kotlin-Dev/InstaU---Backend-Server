package instaU.ayush.com.chat.resource.data

import instaU.ayush.com.chat.data.dao.ChatSessionEntity
import instaU.ayush.com.chat.domain.repository.ChatRepository
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import java.util.UUID

class ConnectToSocketUseCase(private val repository: ChatRepository) {

    suspend operator fun invoke(webSocketServerSession: DefaultWebSocketServerSession) {
        webSocketServerSession.apply {
            val session = call.sessions.get<ChatSessionEntity>()

            if (session == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session found."))
                return@apply
            }

            try {
                repository.connectToSocket(session, this)

                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        repository.sendMessage(
                            Message(
                                messageId = UUID.randomUUID().toString().toLong(),
                                sessionId = session.sessionId,
                                textMessage = frame.readText(),
                                sender = session.sender.toString(),
                                receiver = session.receiver.toString(),
                                timestamp = System.currentTimeMillis().toString()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                repository.disconnectFromSocket(session.sender.toString())
            }
        }
    }
}