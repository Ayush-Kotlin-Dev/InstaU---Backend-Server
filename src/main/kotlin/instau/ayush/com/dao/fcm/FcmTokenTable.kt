package instau.ayush.com.dao.fcm

import instau.ayush.com.dao.post.PostTable.defaultExpression
import instau.ayush.com.dao.post.PostTable.references
import instau.ayush.com.dao.post.PostTable.uniqueIndex
import instau.ayush.com.dao.user.UserTable
import instau.ayush.com.util.CurrentDateTime
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object FcmTokenTable : Table("fcm") {
    val userId = long("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val token = varchar("token", length = 400)

}