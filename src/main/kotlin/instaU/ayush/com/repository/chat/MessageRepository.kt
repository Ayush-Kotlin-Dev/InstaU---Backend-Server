package instaU.ayush.com.repository.chat

import instaU.ayush.com.dao.chat.MessageRow
import instaU.ayush.com.model.ChatMessage
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface MessageRepository {

    suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): MessageRow?

    suspend fun getMessagesForUser(userId: Long): List<MessageRow>

}