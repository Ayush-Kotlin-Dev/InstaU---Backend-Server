package instaU.ayush.com.chat.domain.repository
import instaU.ayush.com.chat.data.dao.ChatSessionEntity
import instaU.ayush.com.chat.resource.data.Message
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow


interface ChatRepository {
    suspend fun getFriendList(): Flow<List<User>>
    suspend fun checkSessionAvailability(sender: String, receiver: String): String?
    suspend fun createNewSession(sender: String, receiver: String): String
    suspend fun sendMessage(request: Message)
    suspend fun getHistoryMessages(sender: String, receiver: String): Flow<List<Message>>
    suspend fun connectToSocket(session: ChatSessionEntity?, socket: WebSocketSession)
    suspend fun disconnectFromSocket(sender: String)
}