package instaU.ayush.com.dao.postlikes

import instaU.ayush.com.dao.post.PostTable
import instaU.ayush.com.dao.user.UserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object PostLikesTable : Table("post_likes") {
    val likeId = long("like_id").uniqueIndex().autoIncrement()
    val postId = long("post_id").references(PostTable.postId , onDelete = ReferenceOption.CASCADE)
    val userId = long("user_id").references(UserTable.id , onDelete = ReferenceOption.CASCADE)
    val likeDate = datetime("created_at").defaultExpression(defaultValue = CurrentDateTime)
}