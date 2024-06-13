package instaU.ayush.com.dao.chat

import instaU.ayush.com.dao.post.PostTable.defaultExpression
import instaU.ayush.com.dao.user.UserTable
import instaU.ayush.com.util.CurrentDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object MessageTable : Table(name = "messages") {
    val id = long(name = "message_id")
    val sessionId = long(name = "session_id")
    val senderId = long(name = "sender_id" )
    val receiverId = long(name = "receiver_id")
    val content = text(name = "content")
    val timestamp = datetime("timestamp").defaultExpression(CurrentDateTime())

}

data class MessageRow(
    val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val content: String,
    val sessionId: Long,
    val timestamp: LocalDateTime
)
