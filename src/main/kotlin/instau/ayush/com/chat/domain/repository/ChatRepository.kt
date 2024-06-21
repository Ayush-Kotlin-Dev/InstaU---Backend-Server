package instau.ayush.com.chat.domain.repository
import instau.ayush.com.chat.data.dao.ChatSessionEntity
import instau.ayush.com.chat.resource.data.Message
import instau.ayush.com.model.chat.User
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow


interface ChatRepository {
    suspend fun getFriendList(sender: Long): Flow<List<User>>
    suspend fun checkSessionAvailability(sender: Long, receiver: Long): Long?
    suspend fun createNewSession(sender: Long, receiver: Long): Long
    suspend fun sendMessage(request: Message)
    suspend fun getHistoryMessages(sender: Long, receiver: Long): Flow<List<Message>>
    suspend fun connectToSocket(session: ChatSessionEntity?, socket: WebSocketSession)
    suspend fun disconnectFromSocket(sender: String)
}