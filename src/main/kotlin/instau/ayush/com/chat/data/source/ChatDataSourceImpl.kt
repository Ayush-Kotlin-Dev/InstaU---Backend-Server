package instau.ayush.com.chat.data.source

import instau.ayush.com.chat.resource.MessageEntity
import instau.ayush.com.chat.resource.data.Message
import instau.ayush.com.dao.DatabaseFactory.dbQuery
import instau.ayush.com.dao.chat.ChatSessionTable
import instau.ayush.com.dao.chat.MessageTable
import instau.ayush.com.dao.user.UserTable
import instau.ayush.com.model.chat.UserEntity
import instau.ayush.com.util.IdGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ChatDataSourceImpl : ChatDataSource {

    override suspend fun getFriendList(sender: Long): Flow<List<UserEntity>> = flow {
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

            val userIds = users.map { it.id }
            val lastMessages = MessageTable
                .select {
                    (MessageTable.senderId eq sender and (MessageTable.receiverId inList userIds)) or
                            (MessageTable.receiverId eq sender and (MessageTable.senderId inList userIds))
                }
                .orderBy(MessageTable.timestamp to SortOrder.DESC)
                .map { row ->
                    val senderId = row[MessageTable.senderId]
                    val receiverId = row[MessageTable.receiverId]
                    val userId = if (senderId == sender) receiverId else senderId
                    userId to Message(
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
                user.copy(lastMessage = lastMessages[user.id])
            }
        }

        emit(friendList)
    }


    override suspend fun createNewSession(sender: Long, receiver: Long): Long {
        val sessionId = IdGenerator.generateId()
        dbQuery {
            ChatSessionTable.insert {
                it[ChatSessionTable.sender] = sender
                it[ChatSessionTable.receiver] = receiver
                it[ChatSessionTable.sessionId] = sessionId
            }
        }
        return sessionId
    }

/**
 * Get session by id
 * This function checks if both sender and receiver already has session id or not.
 */
    override suspend fun checkSessionAvailability(sender: Long, receiver: Long): Long? {
        return transaction {
            ChatSessionTable.select {
                (ChatSessionTable.sender eq sender and (ChatSessionTable.receiver eq receiver)) or
                        (ChatSessionTable.sender eq receiver and (ChatSessionTable.receiver eq sender))
            }.map {
                it[ChatSessionTable.sessionId]
            }.firstOrNull()
        }
    }

    override suspend fun insertMessage(messageEntity: MessageEntity) {
        transaction {
            MessageTable.insert {
                it[id] = messageEntity.messageId
                it[sessionId] = messageEntity.sessionId
                it[content] = messageEntity.textMessage
                it[senderId] = messageEntity.sender
                it[receiverId] = messageEntity.receiver
            }
        }
    }
    /**
     * Get last message
     * This function gets the last message depending on logged-in user and his friend list,
     * as it will return the last sent message between them if available and if not it will return null.
     */
    override suspend fun getHistoryMessages(sender: Long, receiver: Long): Flow<List<MessageEntity>> = flow {
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
}
