package instau.ayush.com.repository.post_likes

import instau.ayush.com.model.LikeParams
import instau.ayush.com.model.LikeResponse
import instau.ayush.com.util.Response

interface PostLikesRepository {
    suspend fun addLike(params: LikeParams): Response<LikeResponse>

    suspend fun removeLike(params: LikeParams): Response<LikeResponse>
}