package instaU.ayush.com.dao.post

import instaU.ayush.com.dao.user.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object  PostTable : Table("posts"){
    val postId = long("post_id").uniqueIndex()
    val caption = varchar("caption" , length = 300)
    val imageUrl = varchar("image_url" , length = 300)
    val likesCount = integer("likes_count")
    val commentsCount = integer("comments_count")
    val userId = long("user_id").references(UserTable.id , onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").defaultExpression(defaultValue = CurrentDateTime)
}

data class PostRow(
    val postId: Long,
    val caption: String,
    val imageUrl: String,
    val likesCount: Int,
    val commentsCount: Int,
    val userId: Long,
    val createdAt: String,
    val userName : String,
    val userImageUrl : String?
)