package instaU.ayush.com.dao.chat

import instaU.ayush.com.model.ChatMessage
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

interface MessageDao {

    suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): MessageRow?

    suspend fun getMessagesForUser(userId: Long): List<MessageRow>

}