package instaU.ayush.com.chat.data.source
import instaU.ayush.com.chat.data.dao.ChatSessionEntity
import instaU.ayush.com.chat.data.source.ChatDataSource
import instaU.ayush.com.chat.resource.MessageEntity
import instaU.ayush.com.chat.resource.data.Message
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
import java.util.*

class ChatDataSourceImpl() : ChatDataSource {


    /**
     * Get friend list
     * Getting friend list with last message sent or received for each friend if exists
     * @param sender
     * @return
     */
    override suspend fun getFriendList(sender: String): Flow<List<UserEntity>> = flow {
        val map = transaction {
            UserTable.selectAll().map { row ->
                UserEntity(
                    id = row[UserTable.id],
                    username = row[UserTable.name],
                    email = row[UserTable.email],
                    avatar = row[UserTable.imageUrl],
                    password = row[UserTable.password],
                    lastMessage = getLastMessage(sender, row[UserTable.email] ?: "")
                )
            }
        }
        emit(map)
    }

    /**
     * Get session by id
     * This function checks if both sender and receiver already has session id or not.
     * @param sender
     * @param receiver
     * @return
     */
    override suspend fun checkSessionAvailability(sender: Long, receiver: Long): String? {
        return transaction {
            ChatSessionTable.select {
                (ChatSessionTable.sender eq sender  and (ChatSessionTable.receiver eq receiver)) or
                        (ChatSessionTable.sender eq receiver and (ChatSessionTable.receiver eq sender))
            }.map { it[ChatSessionTable.sessionId].toString() }
                .firstOrNull()
        }
    }

    /**
     * Create new session
     * This function will create a new session id for sender and receiver and return it back to socket.
     * @param sender
     * @param receiver
     * @return
     */
    override suspend fun createNewSession(sender: String, receiver: String): String {
        val sessionId = UUID.nameUUIDFromBytes(generateNonce().toByteArray()).toString()
        transaction {
            ChatSessionTable.insert {
                it[ChatSessionTable.sender] = sender
                it[ChatSessionTable.receiver] = receiver
                it[ChatSessionTable.sessionId] = sessionId
            }
        }
        return sessionId
    }

    override suspend fun insertMessage(messageEntity: MessageEntity) {
        transaction {
            MessageTable.insert {
                it[MessageTable.sessionId] = messageEntity.sessionId
                it[MessageTable.content] = messageEntity.textMessage
                it[MessageTable.senderId] = messageEntity.sender
                it[MessageTable.receiverId] = messageEntity.receiver
                it[MessageTable.timestamp] = messageEntity.timestamp
            }
        }
    }

    /**
     * Get history messages
     * This functions gets room history depending on sender and receiver session id availability,
     * if there is a session id, then it will fetch related messages, if not it will return empty list.
     * @param sender
     * @param receiver
     * @return
     */
    override suspend fun getHistoryMessages(sender: String, receiver: String): Flow<List<MessageEntity>> = flow {
        val result = transaction {
            MessageTable.select {
                (MessageTable.senderId eq sender and (MessageTable.receiverId eq receiver)) or
                        (MessageTable.senderId eq receiver and (MessageTable.receiverId eq sender))
            }.orderBy(MessageTable.timestamp to SortOrder.DESC)
                .map {
                    MessageEntity(
                        messageId = it[MessageTable.id].toString().toLong(),
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

    /**
     * Get last message
     * This function gets the last message depending on logged-in user and his friend list,
     * as it will return the last sent message between them if available and if not it will return null.
     * @param sender
     * @param receiver
     * @return
     */
    private fun getLastMessage(sender: String, receiver: String): Message? {
        return transaction {
            MessageTable.select {
                (MessageTable.senderId eq sender and (MessageTable.receiverId eq receiver)) or
                        (MessageTable.senderId eq receiver and (MessageTable.receiverId eq sender))
            }.orderBy(MessageTable.timestamp to SortOrder.DESC)
                .limit(1)
                .map {
                    Message(
                        messageId = UUID.randomUUID().toString().toLong(),
                        sessionId = it[MessageTable.sessionId],
                        textMessage = it[MessageTable.content],
                        sender = it[MessageTable.senderId],
                        receiver = it[MessageTable.receiverId],
                        timestamp = it[MessageTable.timestamp].toString()
                    )
                }.firstOrNull()
        }
    }
}