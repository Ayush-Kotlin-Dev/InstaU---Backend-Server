package instaU.ayush.com.dao.post

interface PostDao {


    suspend fun createPost(userId: Long, imageUrl: String, caption: String): Boolean

    suspend fun getFeedsPost (userId: Long, follows : List<Long> , pageNumber: Int, pageSize: Int): List<PostRow>

    suspend fun getPostByUser(userId: Long, pageNumber: Int, pageSize: Int): List<PostRow>

    suspend fun getPost(postId: Long): PostRow?

    suspend fun delete( postId: Long): Boolean
}