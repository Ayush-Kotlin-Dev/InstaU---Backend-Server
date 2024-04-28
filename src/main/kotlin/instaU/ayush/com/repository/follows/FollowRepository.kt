package instaU.ayush.com.repository.follows

import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.util.Response

interface FollowRepository {

    suspend fun followUser(follower: Long, following: Long): Response<FollowsAndUnfollowsResponse>

    suspend fun unFollowUser(follower: Long, following: Long): Response<FollowsAndUnfollowsResponse>

}