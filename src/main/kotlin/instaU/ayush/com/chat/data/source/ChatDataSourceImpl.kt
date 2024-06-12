package instaU.ayush.com.chat.data.source

import instaU.ayush.com.chat.data.dao.ChatSessionEntity
import instaU.ayush.com.chat.resource.MessageEntity
import instaU.ayush.com.chat.resource.data.Message
import instaU.ayush.com.dao.DatabaseFactory.dbQuery
import instaU.ayush.com.dao.chat.ChatSessionTable
import instaU.ayush.com.dao.chat.MessageTable
import instaU.ayush.com.dao.user.UserTable
import instaU.ayush.com.model.chat.UserEntity
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatDataSourceImpl : ChatDataSource {

    override suspend fun getFriendList(sender: String): Flow<List<UserEntity>> = flow {
        val friendList = dbQuery {
            val users = UserTable.selectAll().map { row ->
                UserEntity(
                    id = row[UserTable.id],
                    username = row[UserTable.name],
                    email = row[UserTable.email],
                    avatar = row[UserTable.imageUrl],
                    password = row[UserTable.password],
                    lastMessage = null
                )
            }

            val userIds = users.map { it.email }
            val lastMessages = MessageTable
                .select { (MessageTable.senderId inList userIds) or (MessageTable.receiverId inList userIds) }
                .orderBy(MessageTable.timestamp to SortOrder.DESC)
                .map { row ->
                    val senderId = row[MessageTable.senderId]
                    val receiverId = row[MessageTable.receiverId]
                    val email = if (senderId == sender) receiverId else senderId
                    email to Message(
                        messageId = row[MessageTable.id].toString().toLong(),
                        sessionId = row[MessageTable.sessionId],
                        textMessage = row[MessageTable.content],
                        sender = senderId,
                        receiver = receiverId,
                        timestamp = row[MessageTable.timestamp].toString()
                    )
                }
                .groupBy { it.first }
                .mapValues { it.value.first().second }

            users.map { user ->
                user.copy(lastMessage = lastMessages[user.email])
            }
        }

        emit(friendList)
    }

    override suspend fun createNewSession(sender: Long, receiver: Long): String {
        val sessionId = UUID.nameUUIDFromBytes(generateNonce().toByteArray()).toString().toLong()
        dbQuery {
            ChatSessionTable.insert {
                it[ChatSessionTable.sender] = sender
                it[ChatSessionTable.receiver] = receiver
                it[ChatSessionTable.sessionId] = sessionId
            }
        }
        return sessionId.toString()
    }

    override suspend fun checkSessionAvailability(sender: Long, receiver: Long): String? {
        return transaction {
            ChatSessionTable.select {
                (ChatSessionTable.sender eq sender and (ChatSessionTable.receiver eq receiver)) or
                        (ChatSessionTable.sender eq receiver and (ChatSessionTable.receiver eq sender))
            }.map {
                it[ChatSessionTable.sessionId].toString()
            }.firstOrNull()
        }
    }

    override suspend fun insertMessage(messageEntity: MessageEntity) {
        transaction {
            MessageTable.insert {
                it[sessionId] = messageEntity.sessionId
                it[content] = messageEntity.textMessage
                it[senderId] = messageEntity.sender
                it[receiverId] = messageEntity.receiver
                it[timestamp] = LocalDateTime.parse(messageEntity.timestamp, DateTimeFormatter.ofPattern("your-timestamp-format"))
            }
        }
    }

    override suspend fun getHistoryMessages(sender: String, receiver: String): Flow<List<MessageEntity>> = flow {
        val result = dbQuery {
            MessageTable.select {
                (MessageTable.senderId eq sender and (MessageTable.receiverId eq receiver)) or
                        (MessageTable.senderId eq receiver and (MessageTable.receiverId eq sender))
            }.orderBy(MessageTable.timestamp to SortOrder.DESC)
                .map {
                    MessageEntity(
                        messageId = it[MessageTable.id],
                        sessionId = it[MessageTable.sessionId],
                        textMessage = it[MessageTable.content],
                        sender = it[MessageTable.senderId],
                        receiver = it[MessageTable.receiverId],
                        timestamp = it[MessageTable.timestamp].toString()
                    )
                }
        }
        emit(result)
    }

    private suspend fun getLastMessage(sender: String, receiver: String): Message? {
        return dbQuery {
            MessageTable.select {
                (MessageTable.senderId eq sender and (MessageTable.receiverId eq receiver)) or
                        (MessageTable.senderId eq receiver and (MessageTable.receiverId eq sender))
            }.orderBy(MessageTable.timestamp to SortOrder.DESC)
                .limit(1)
                .map {
                    Message(
                        messageId = it[MessageTable.id].toString().toLong(),
                        sessionId = it[MessageTable.sessionId],
                        textMessage = it[MessageTable.content],
                        sender = it[MessageTable.senderId].toString(),
                        receiver = it[MessageTable.receiverId].toString(),
                        timestamp = it[MessageTable.timestamp].toString()
                    )
                }.firstOrNull()
        }
    }
}
