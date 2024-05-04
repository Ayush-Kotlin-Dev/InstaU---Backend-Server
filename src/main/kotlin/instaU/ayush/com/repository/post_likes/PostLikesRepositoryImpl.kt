package instaU.ayush.com.repository.post_likes

import instaU.ayush.com.dao.post.PostDao
import instaU.ayush.com.dao.postlikes.PostLikesDao
import instaU.ayush.com.model.LikeParams
import instaU.ayush.com.model.LikeResponse
import instaU.ayush.com.util.Response
import io.ktor.http.*

class PostLikesRepositoryImpl(
    private val likesDao: PostLikesDao,
    private val postDao: PostDao
) : PostLikesRepository {
    override suspend fun addLike(params: LikeParams): Response<LikeResponse> {
        val likeExists = likesDao.isPostLikedByUser(postId = params.postId, userId = params.userId)
        return if (likeExists){
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = LikeResponse(success = false, message = "Post already liked")
            )
        }else{
            val postLiked = likesDao.addLike(postId = params.postId, userId = params.userId)
            if (postLiked){
                postDao.updateLikesCount(postId = params.postId)
                Response.Success(
                    data = LikeResponse(success = true)
                )
            }else{
                Response.Error(
                    code = HttpStatusCode.Conflict,
                    data = LikeResponse(success = false, message = "Unexpected DB error, try again!")
                )
            }
        }
    }

    override suspend fun removeLike(params: LikeParams): Response<LikeResponse> {
        val likeExists = likesDao.isPostLikedByUser(postId = params.postId, userId = params.userId)
        return if (likeExists){
            val likeRemoved = likesDao.removeLike(postId = params.postId, userId =  params.userId)
            if (likeRemoved){
                postDao.updateLikesCount(postId = params.postId, decrement = true)
                Response.Success(
                    data = LikeResponse(success = true)
                )
            }else{
                Response.Error(
                    code = HttpStatusCode.Conflict,
                    data = LikeResponse(success = false, message = "Unexpected DB error, try again!")
                )
            }
        }else{
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = LikeResponse(success = false, message = "Like not found(may be removed already)")
            )
        }
    }
}