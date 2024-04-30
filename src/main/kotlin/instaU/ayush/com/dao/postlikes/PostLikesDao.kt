package instaU.ayush.com.dao.postlikes

interface PostLikesDao {

    suspend fun addLike(postId: Long, userId: Long): Boolean

    suspend fun removeLike(postId: Long, userId: Long): Boolean

    suspend fun isPostLikedByUser(postId: Long, userId: Long): Boolean

}