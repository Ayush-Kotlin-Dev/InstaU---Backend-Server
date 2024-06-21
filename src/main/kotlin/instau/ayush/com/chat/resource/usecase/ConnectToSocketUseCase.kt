package instau.ayush.com.chat.resource.data

import instau.ayush.com.chat.data.dao.ChatSessionEntity
import instau.ayush.com.chat.domain.repository.ChatRepository
import instau.ayush.com.util.IdGenerator
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

class ConnectToSocketUseCase(private val repository: ChatRepository) {
    //TODO
    //Will check weather user is exists in DB or not
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
                                messageId = IdGenerator.generateId(),
                                sessionId = session.sessionId,
                                textMessage = frame.readText(),
                                sender = session.sender,
                                receiver = session.receiver,
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