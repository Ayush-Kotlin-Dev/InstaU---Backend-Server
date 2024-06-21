package instau.ayush.com.repository.follows

import instau.ayush.com.model.FollowAndUnfollowResponse
import instau.ayush.com.model.GetFollowsResponse
import instau.ayush.com.util.Response

interface FollowRepository {

    suspend fun followUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse>

    suspend fun unFollowUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse>

    suspend fun getFollowers(userId: Long, pageNumber: Int, pageSize: Int): Response<GetFollowsResponse>

    suspend fun getFollowing(userId: Long, pageNumber: Int, pageSize: Int): Response<GetFollowsResponse>

    suspend fun getFollowingSuggestions(userId: Long): Response<GetFollowsResponse>

}