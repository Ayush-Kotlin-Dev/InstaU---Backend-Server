package instaU.ayush.com.repository.post

import instaU.ayush.com.dao.post.PostRow
import instaU.ayush.com.model.PostResponse
import instaU.ayush.com.model.PostTextParams
import instaU.ayush.com.model.PostsResponse
import instaU.ayush.com.util.Response
import org.jetbrains.exposed.sql.ResultRow

interface PostRepository {

    suspend fun createPost(postTextParams: PostTextParams): Response<PostResponse>

    suspend fun getFeedsPost(userId: Long, pageNumber: Int, pageSize: Int): Response<PostsResponse>

    suspend fun getPostByUser(
        postOwnerId: Long, currentUserId: Long, pageNumber: Int, pageSize: Int
    ): Response<PostsResponse>


    suspend fun getPost(postId: Long, currentUserId: Long): Response<PostResponse>


    suspend fun deletePost(postId: Long): Response<PostResponse>

    suspend fun notifyClient(message : String)
}