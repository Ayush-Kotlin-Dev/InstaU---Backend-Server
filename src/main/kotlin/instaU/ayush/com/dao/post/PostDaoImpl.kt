package instaU.ayush.com.dao.post

class PostDaoImpl : PostDao {
    override suspend fun createPost(userId: Long, imageUrl: String, caption: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getFeedsPost(
        userId: Long,
        follows: List<Long>,
        pageNumber: Int,
        pageSize: Int
    ): List<PostRow> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostByUser(userId: Long, pageNumber: Int, pageSize: Int): List<PostRow> {
        TODO("Not yet implemented")
    }

    override suspend fun getPost(postId: Long): PostRow? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(postId: Long): Boolean {
        TODO("Not yet implemented")
    }
}