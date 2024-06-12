package instaU.ayush.com.dao.chat

import instaU.ayush.com.dao.user.UserTable
import org.jetbrains.exposed.sql.Table

object ChatSessionTable : Table(name = "chat_sessions") {
    val sender = long(name = "sender_id")
    val receiver = long(name = "receiver_id")
    val sessionId = long(name = "session_id")
}