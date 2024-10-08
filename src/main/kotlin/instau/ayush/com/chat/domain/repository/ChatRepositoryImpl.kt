package instau.ayush.com.chat.domain.repository

import instau.ayush.com.chat.data.dao.ChatSessionEntity
import instau.ayush.com.chat.data.source.ChatDataSource
import instau.ayush.com.chat.resource.data.Member
import instau.ayush.com.chat.resource.data.Message
import instau.ayush.com.chat.resource.data.toMessage
import instau.ayush.com.chat.resource.data.toMessageEntity
import instau.ayush.com.model.chat.User
import instau.ayush.com.model.chat.UserData
import instau.ayush.com.model.chat.UserEntity
import instau.ayush.com.util.NotificationService
import io.ktor.websocket.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class ChatRepositoryImpl(
    private val datasource: ChatDataSource,
    private val notificationService: NotificationService // Inject Notification Service

) : ChatRepository {

    private val members = ConcurrentHashMap<String, Member>()

    override suspend fun getFriendList(
        sender: Long
    ): Flow<List<User>> = flow {
        datasource.getFriendList(sender).collect { friendList ->
            val friendListResult = friendList.filter { friendEntity ->
                friendEntity.id != sender
            }.map { it.toUser() }
            emit(friendListResult)
        }
    }

    override suspend fun checkSessionAvailability(sender: Long, receiver: Long): Long? =
        datasource.checkSessionAvailability(sender, receiver)

    override suspend fun createNewSession(sender: Long, receiver: Long): Long =
        datasource.createNewSession(sender, receiver)

    override suspend fun sendMessage(request: Message) {
        datasource.insertMessage(request.toMessageEntity())

        val broadcastMessage = Json.encodeToString(request)

        var receiverConnected = false

        println("Sending message. SessionId: ${request.sessionId}, Sender: ${request.sender}, Receiver: ${request.receiver}")
        println("Current members: ${members.keys}")

        members.values.filter { it.sessionId == request.sessionId }.forEach { member ->
            println("Sending to member: ${member.sender}")
            member.webSocket.send(Frame.Text(broadcastMessage))
            if (member.sender == request.receiver.toString()) {
                receiverConnected = true
                println("Receiver is connected")
            }
        }

        if (!receiverConnected) {
            println("Receiver not connected. Sending notification ${request.receiver}")
            coroutineScope {
                launch {
                    try {
                        notificationService.sendNotificationToReceiver(request.sender, request.receiver, request.textMessage)
                        println("Notification sent successfully")
                    } catch (e: Exception) {
                        println("Error sending notification: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
        } else {
            println("Receiver is connected. No notification sent.")
        }
    }


    override suspend fun getHistoryMessages(sender: Long, receiver: Long)
            : Flow<List<Message>> = flow {
        datasource.getHistoryMessages(sender, receiver).collect { messageEntityList ->
            val messageListResult = messageEntityList.map {
                it.toMessage()
            }
            emit(messageListResult)
        }
    }

    override suspend fun connectToSocket(session: ChatSessionEntity?, socket: WebSocketSession) {
        if (members.contains(session?.sender.toString()))
            println("User exists")

        members[session?.sender.toString()] = Member(
            sender = session?.sender.toString(),
            sessionId = session?.sessionId!!,
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
    user = UserData(
        username = username,
        userId = id,
        email = email,
        avatar = avatar,
        lastMessage = lastMessage
    )
)