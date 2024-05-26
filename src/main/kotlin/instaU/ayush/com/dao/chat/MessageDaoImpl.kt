package instaU.ayush.com.dao.chat

import instaU.ayush.com.repository.chat.MessageRepository
import instaU.ayush.com.repository.chat.MessageRepositoryImpl
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime


class MessageDaoImpl : MessageDao {

    override suspend fun sendMessage(senderId: Long, receiverId: Long, content: String): MessageRow? {
        return transaction {
            val insertStatement = MessageTable.insert {
                it[this.senderId] = senderId
                it[this.receiverId] = receiverId
                it[this.content] = content
                it[this.timestamp] = LocalDateTime.now()
            }

            val messageId = insertStatement[MessageTable.id]
            MessageTable.select { MessageTable.id eq messageId }
                .mapNotNull { toMessageRow(it) }
                .singleOrNull()
        }
    }

    override suspend fun getMessagesForUser(userId: Long): List<MessageRow> {
        return transaction {
            MessageTable.select { MessageTable.receiverId eq userId }
                .mapNotNull { toMessageRow(it) }
        }
    }

    private fun toMessageRow(row: ResultRow): MessageRow {
        return MessageRow(
            id = row[MessageTable.id],
            senderId = row[MessageTable.senderId],
            receiverId = row[MessageTable.receiverId],
            content = row[MessageTable.content],
            timestamp = row[MessageTable.timestamp]
        )
    }
}
