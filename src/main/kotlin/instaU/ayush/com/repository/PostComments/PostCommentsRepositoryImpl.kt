package instaU.ayush.com.repository.PostComments

import instaU.ayush.com.dao.PostComment.PostCommentRow
import instaU.ayush.com.dao.PostComment.PostCommentsDao
import instaU.ayush.com.dao.post.PostDao
import instaU.ayush.com.model.*
import instaU.ayush.com.util.Response
import io.ktor.http.*

class PostCommentsRepositoryImpl(
    private val commentDao : PostCommentsDao,
    private val postDao : PostDao
) : PostCommentsRepository {
    override suspend fun addComment(params: NewCommentParams): Response<CommentResponse> {
       val postCommentRow = commentDao.addComment(params.postId, params.userId, params.content)
        return if (postCommentRow == null) {
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = CommentResponse(
                    success = false,
                    message = "Could not add comment!"
                )
            )
        } else {
            postDao.updateCommentsCount(params.postId)
            Response.Success(
                data = CommentResponse(
                    success = true,
                    comment = postCommentRow.toPostComment()
                )
            )
        }
    }

    override suspend fun removeComment(params: RemoveCommentParams): Response<CommentResponse> {
        val commentRow = commentDao.findComment(params.commentId, params.postId)

        return if (commentRow == null) {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = CommentResponse(
                    success = false,
                    message = "Comment not found!"
                )
            )
        } else if (commentRow.userId != params.userId) {
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = CommentResponse(
                    success = false,
                    message = "You are not allowed to delete this comment!"
                )
            )
        } else {
            val isDeleted = commentDao.removeComment(params.commentId, params.postId)
            if (isDeleted) {
                postDao.updateCommentsCount(params.postId, true)
                Response.Success(
                    data = CommentResponse(
                        success = true,
                        message = "Comment deleted successfully!"
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = CommentResponse(
                        success = false,
                        message = "Could not delete comment!"
                    )
                )
            }
        }
    }

    override suspend fun getComments(postId: Long, pageNumber: Int, pageSize: Int): Response<GetCommentResponse> {
        val commentRows = commentDao.getComments(postId, pageNumber, pageSize)
        val comments = commentRows.map { it.toPostComment() }
return Response.Success(
            data = GetCommentResponse(
                success = true,
                comments = comments
            )
        )

    }

    private fun PostCommentRow.toPostComment() = PostComment(
        commentId = this.commentId,
        content = this.content,
        postId = this.postId,
        userId = this.userId,
        userName = this.userName,
        userImageUrl = this.userImageUrl,
        createdAt = this.createdAt
    )
}