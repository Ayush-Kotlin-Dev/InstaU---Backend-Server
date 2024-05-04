package instaU.ayush.com.repository.post_likes

import instaU.ayush.com.model.LikeParams
import instaU.ayush.com.model.LikeResponse
import instaU.ayush.com.util.Response

interface PostLikesRepository {
    suspend fun addLike(params: LikeParams): Response<LikeResponse>

    suspend fun removeLike(params: LikeParams): Response<LikeResponse>
}