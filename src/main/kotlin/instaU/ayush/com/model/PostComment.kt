package instaU.ayush.com.model

import kotlinx.serialization.Serializable

@Serializable
data class NewCommentParams(
    val postId: Long,
    val userId: Long,
    val content : String
)
@Serializable
data class RemoveCommentParams(
    val commentId: Long,
    val postId: Long,
    val userId : Long
)
@Serializable
data class PostComment(
    val commentId: Long,
    val content : String,
    val postId: Long,
    val userId: Long,
    val userName : String,
    val userImageUrl : String ?,
    val createdAt: String
)
@Serializable
data class CommentResponse(
    val success: Boolean,
    val comment: PostComment? = null,
    val message: String? = null
)

@Serializable
data class GetCommentResponse(
    val success: Boolean,
    val comments: List<PostComment>? = null,
    val message: String? = null
)