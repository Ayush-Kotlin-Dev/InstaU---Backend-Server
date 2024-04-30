package instaU.ayush.com.repository.post

import instaU.ayush.com.dao.post.PostDao
import instaU.ayush.com.dao.user.UserDao
import instaU.ayush.com.model.PostResponse
import instaU.ayush.com.model.PostTextParams
import instaU.ayush.com.model.PostsResponse
import instaU.ayush.com.util.Response
import io.ktor.http.*

class PostRepositoryImpl(
    private val postDao: PostDao,
    private val userDap : UserDao
) : PostRepository {
    override suspend fun createPost(imageUrl: String, postTextParams: PostTextParams): Response<PostResponse> {
        val postIsCreated = postDao.createPost(
            userId = postTextParams.userId,
            imageUrl = imageUrl,
            caption = postTextParams.caption,
        )
        return if (postIsCreated) {
            Response.Success(
                data = PostResponse(
                    success = true,
                    message = "Post created successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponse(
                    success = false,
                    message = "Failed to create post"
                )
            )
        }
    }

    override suspend fun getFeedsPost(userId: Long, pageNumber: Int, pageSize: Int): Response<PostsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostByUser(
        postOwnerId: Long,
        currentUserId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Response<PostsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPost(postId: Long, currentUserId: Long): Response<PostResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePost(postId: Long): Response<PostResponse> {
        val postIsDeleted = postDao.deletePost(
            postId
        )
        return if (postIsDeleted) {
            Response.Success(
                data = PostResponse(
                    success = true,
                    message = "Post deleted successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponse(
                    success = false,
                    message = "Failed to delete post"
                )
            )
        }
    }
}