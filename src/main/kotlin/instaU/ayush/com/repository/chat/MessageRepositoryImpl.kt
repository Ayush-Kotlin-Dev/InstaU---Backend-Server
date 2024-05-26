package instaU.ayush.com.repository.chat

import instaU.ayush.com.dao.chat.MessageDao
import instaU.ayush.com.dao.chat.MessageRow
import instaU.ayush.com.dao.chat.MessageTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class MessageRepositoryImpl(
    private val messageDao: MessageDao
) : MessageRepository {


override suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): MessageRow? {
    return messageDao.sendMessage(senderId, receiverId, content)
}

override suspend fun getMessagesForUser(userId: Long): List<MessageRow> {
    return messageDao.getMessagesForUser(userId)

}
}