package instaU.ayush.com.chat.domain.repository
import instaU.ayush.com.chat.data.dao.ChatSessionEntity
import instaU.ayush.com.chat.data.source.ChatDataSource
import instaU.ayush.com.chat.resource.data.Member
import instaU.ayush.com.chat.resource.data.Message
import instaU.ayush.com.chat.resource.data.toMessage
import instaU.ayush.com.chat.resource.data.toMessageEntity
import instaU.ayush.com.model.chat.User
import instaU.ayush.com.model.chat.UserData
import instaU.ayush.com.model.chat.UserEntity
import io.ktor.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import java.util.concurrent.ConcurrentHashMap

class ChatRepositoryImpl(
    private val datasource: ChatDataSource
) : ChatRepository {

    private val members = ConcurrentHashMap<String, Member>()

    override suspend fun getFriendList(
        sender: String
    ): Flow<List<User>> = flow {
        datasource.getFriendList( sender.toString(){ friendList ->
            val friendListResult = friendList.filter { friendEntity ->
                friendEntity.email != sender
            }.map { it.toUser() }
            emit(friendListResult)
        }
    }

    override suspend fun checkSessionAvailability(sender: String, receiver: String): String? =
        datasource.checkSessionAvailability(sender, receiver)

    override suspend fun createNewSession(sender: String, receiver: String): String =
        datasource.createNewSession(sender, receiver)

    override suspend fun sendMessage(request: Message) {
        datasource.insertMessage(request.toMessageEntity())

        members.values.filter { it.sessionId == request.sessionId }.forEach { member ->
            // Encoding message into json string.
            val broadcastMessage = Json.encodeToString(request)
            // Sending message to other socket subscribers.
            member.webSocket.send(Frame.Text(broadcastMessage))
        }
    }

    override suspend fun getHistoryMessages(sender: String, receiver: String)
            : Flow<List<Message>> = flow {
        datasource.getHistoryMessages(sender, receiver).collect { messageEntityList ->
            val messageListResult = messageEntityList.map {
                it.toMessage()
            }
            emit(messageListResult)
        }
    }

    override suspend fun connectToSocket(session: ChatSessionEntity?, socket: WebSocketSession) {
        if (members.contains(session?.sender))
            println("User exists")

        members[session?.sender.orEmpty()] = Member(
            sender = session?.sender.orEmpty(),
            sessionId = session?.sessionId.orEmpty(),
            webSocket = socket
        )
    }

    override suspend fun disconnectFromSocket(sender: String) {
        // closing websocket for a subscribed user
        members[sender]?.webSocket?.close(CloseReason(CloseReason.Codes.NORMAL, "Peer left."))

        // Removing user from socket
        if (members.containsKey(sender))
            members.remove(sender)
    }
}
fun UserEntity.toUser() = User(
    token = token,
    user = UserData(
        username = username,
        email = email,
        avatar = avatar,
        lastMessage = lastMessage
    )
)