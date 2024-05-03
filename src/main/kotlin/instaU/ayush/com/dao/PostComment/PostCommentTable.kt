package instaU.ayush.com.dao.PostComment

import instaU.ayush.com.dao.post.PostTable
import instaU.ayush.com.dao.user.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object PostCommentTable : Table(name = "post_comments") {
    val commentId = long(name = "comment_id").uniqueIndex()
    val postId = long(name = "post_id").references(ref = PostTable.postId, onDelete = ReferenceOption.CASCADE)
    val userId = long(name = "user_id").references(ref = UserTable.id, onDelete = ReferenceOption.CASCADE)
    val comment = varchar(name = "comment", length = 250)
    val createdAt = datetime(name = "created_at").defaultExpression(defaultValue = CurrentDateTime)
}

data class PostCommentRow(
    val commentId: Long,
    val content : String,
    val postId: Long,
    val userId: Long,
    val userName : String,
    val userImageUrl : String ?,
    val createdAt: String
)