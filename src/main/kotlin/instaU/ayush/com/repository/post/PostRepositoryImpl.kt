package instaU.ayush.com.repository.post

import instaU.ayush.com.dao.follows.FollowsDao
import instaU.ayush.com.dao.post.PostDao
import instaU.ayush.com.dao.post.PostRow
import instaU.ayush.com.dao.postlikes.PostLikesDao
import instaU.ayush.com.dao.user.UserDao
import instaU.ayush.com.model.Post
import instaU.ayush.com.model.PostResponse
import instaU.ayush.com.model.PostTextParams
import instaU.ayush.com.model.PostsResponse
import instaU.ayush.com.util.Response
import io.ktor.http.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.collections.LinkedHashSet

val connectedClients = Collections.synchronizedSet(LinkedHashSet<DefaultWebSocketServerSession>())

class PostRepositoryImpl(
    private val postDao: PostDao,
    private val followDao: FollowsDao,
    private val postLikesDao: PostLikesDao

) : PostRepository {
    override suspend fun createPost(postTextParams: PostTextParams): Response<PostResponse> {
        val postIsCreated = postDao.createPost(
            userId = postTextParams.userId,
            imageUrl = postTextParams.imageUrl,
            caption = postTextParams.caption,
        )
        return if (postIsCreated) {
            notifyClient("added")
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
        val followingUsers = followDao.getAllFollowing(userId).toMutableList()
        followingUsers.add(userId)
        val postsRows = postDao.getFeedsPost(
            userId = userId,
            follows = followingUsers,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
        val posts = postsRows.map {
            toPost(
                postRow = it,
                isPostLiked = postLikesDao.isPostLikedByUser(it.postId, userId),
                isOwnPost = it.userId == userId
            )
        }
        return Response.Success(
            data = PostsResponse(
                success = true,
                posts = posts
            )
        )
    }

    override suspend fun getPostByUser(
        postOwnerId: Long,
        currentUserId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Response<PostsResponse> {
        val postsRows = postDao.getPostByUser(
            userId = postOwnerId,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
        val posts = postsRows.map {
            toPost(
                postRow = it,
                isPostLiked = postLikesDao.isPostLikedByUser(it.postId, currentUserId),
                isOwnPost = it.userId == currentUserId
            )
        }
        return Response.Success(
            data = PostsResponse(
                success = true,
                posts = posts
            )
        )
    }

    override suspend fun getPost(postId: Long, currentUserId: Long): Response<PostResponse> {
        val post = postDao.getPost(postId)
        return if (post != null) {
            val isPostLiked = postLikesDao.isPostLikedByUser(postId, currentUserId)
            val isOwnPost = post.userId == currentUserId
            Response.Success(
                data = PostResponse(
                    success = true,
                    post = toPost(post, isPostLiked, isOwnPost)
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = PostResponse(
                    success = false,
                    message = "Post not found"
                )
            )
        }
    }

    override suspend fun deletePost(postId: Long): Response<PostResponse> {
        val postIsDeleted = postDao.deletePost(
            postId
        )
        return if (postIsDeleted) {
            notifyClient("deleted")
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

    override suspend fun notifyClient(message: String) {
        connectedClients.forEach {
            it.send(Frame.Text(message))
        }
    }

    private fun toPost(postRow: PostRow, isPostLiked: Boolean, isOwnPost: Boolean): Post {
        return Post(
            postId = postRow.postId,
            caption = postRow.caption,
            imageUrl = postRow.imageUrl,
            createdAt = postRow.createdAt,
            likesCount = postRow.likesCount,
            commentsCount = postRow.commentsCount,
            userId = postRow.userId,
            userName = postRow.userName,
            userImageUrl = postRow.userImageUrl,
            isLiked = isPostLiked,
            isOwnPost = isOwnPost
        )
    }
}

//data class PostChange(
// val action: String,
// val postId: String
// )
