package instaU.ayush.com.model

import kotlinx.serialization.Serializable

@Serializable
data class FollowsAndUnfollowsResponse(
    val success: Boolean,
    val message: String? = null
)

@Serializable
data class FollowsParams(
    val follower : Long ,
    val following : Long,
    val isFollowing : Boolean

)