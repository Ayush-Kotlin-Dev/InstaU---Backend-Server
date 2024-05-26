package instaU.ayush.com.repository.chat

import instaU.ayush.com.dao.chat.MessageRow
import instaU.ayush.com.model.ChatMessage
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface MessageRepository {

    suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): MessageRow?

    suspend fun getMessagesForUser(userId: Long): List<MessageRow>

}
object ChatMessages : Table() {
    val id = long("id").autoIncrement()
    val sender = varchar("sender", 255)
    val recipient = varchar("recipient", 255)
    val message = varchar("message", 1000)
    val timestamp = long("timestamp")
}
