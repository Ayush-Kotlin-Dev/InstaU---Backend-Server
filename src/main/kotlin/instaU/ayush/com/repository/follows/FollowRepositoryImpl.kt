package instaU.ayush.com.repository.follows

import instaU.ayush.com.dao.follows.FollowsDao
import instaU.ayush.com.dao.user.UserDao
import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.util.Response
import io.ktor.http.*

class FollowRepositoryImpl(
    private val userDao: UserDao,
    private val followDao: FollowsDao
) : FollowRepository {
    override suspend fun followUser(follower: Long, following: Long): Response<FollowsAndUnfollowsResponse> {
        return if (followDao.isAlreadyFollowing(follower, following)) {
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = FollowsAndUnfollowsResponse(
                    success = false,
                    message = "Already following"
                )
            )
        } else {
            val success = followDao.followUser(follower, following)

            if (success) {
                userDao.updateFollowsCount(follower, following, isFollowing = true)
                Response.Success(
                    data = FollowsAndUnfollowsResponse(
                        success = true,
                        message = "Followed"
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = FollowsAndUnfollowsResponse(
                        success = false,
                        message = "Failed to follow"
                    )
                )
            }
        }
    }

    override suspend fun unFollowUser(follower: Long, following: Long): Response<FollowsAndUnfollowsResponse> {
         val success = followDao.unFollowUser(follower, following)

        return if (success) {
            userDao.updateFollowsCount(follower, following, isFollowing = false)
            Response.Success(
                data = FollowsAndUnfollowsResponse(
                    success = true,
                    message = "Unfollowed"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = FollowsAndUnfollowsResponse(
                    success = false,
                    message = "Failed to unfollow"
                )
            )
        }
    }
}