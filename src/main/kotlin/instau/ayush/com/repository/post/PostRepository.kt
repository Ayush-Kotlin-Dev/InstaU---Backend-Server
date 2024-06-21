package instau.ayush.com.repository.post

import instau.ayush.com.model.PostResponse
import instau.ayush.com.model.PostTextParams
import instau.ayush.com.model.PostsResponse
import instau.ayush.com.util.Response

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