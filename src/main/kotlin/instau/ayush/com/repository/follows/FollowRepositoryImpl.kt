package instau.ayush.com.repository.follows

import instau.ayush.com.dao.follows.FollowsDao
import instau.ayush.com.dao.user.UserDao
import instau.ayush.com.dao.user.UserRow
import instau.ayush.com.model.FollowAndUnfollowResponse
import instau.ayush.com.model.FollowUserData
import instau.ayush.com.model.GetFollowsResponse
import instau.ayush.com.util.Constants
import instau.ayush.com.util.Response
import io.ktor.http.*

class FollowRepositoryImpl(
    private val userDao: UserDao,
    private val followDao: FollowsDao
) : FollowRepository {
    override suspend fun followUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse> {
        return if (followDao.isAlreadyFollowing(follower, following)) {
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = FollowAndUnfollowResponse(
                    success = false,
                    message = "Already following"
                )
            )
        } else {
            val success = followDao.followUser(follower, following)

            if (success) {
                userDao.updateFollowsCount(follower, following, isFollowing = true)
                Response.Success(
                    data = FollowAndUnfollowResponse(
                        success = true,
                        message = "Followed"
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = FollowAndUnfollowResponse(
                        success = false,
                        message = "Failed to follow"
                    )
                )
            }
        }
    }

    override suspend fun unFollowUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse> {
         val success = followDao.unFollowUser(follower, following)

        return if (success) {
            userDao.updateFollowsCount(follower, following, isFollowing = false)
            Response.Success(
                data = FollowAndUnfollowResponse(
                    success = true,
                    message = "Unfollowed"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = FollowAndUnfollowResponse(
                    success = false,
                    message = "Failed to unfollow"
                )
            )
        }
    }

    override suspend fun getFollowers(userId: Long, pageNumber: Int, pageSize: Int): Response<GetFollowsResponse> {
        val followersIds = followDao.getFollowers(userId, pageNumber, pageSize)
        val followersRows = userDao.getUsers(ids = followersIds)
        val followers = followersRows.map { followerRow ->
            val isFollowing = followDao.isAlreadyFollowing(follower = userId, following = followerRow.id)
            toFollowUserData(userRow = followerRow, isFollowing = isFollowing)
        }
        return Response.Success(
            data = GetFollowsResponse(success = true, follows = followers)
        )
    }

    override suspend fun getFollowing(userId: Long, pageNumber: Int, pageSize: Int): Response<GetFollowsResponse> {
        val followingIds = followDao.getFollowing(userId, pageNumber, pageSize)
        val followingRows = userDao.getUsers(ids = followingIds)
        val following = followingRows.map { followingRow ->
            toFollowUserData(userRow = followingRow, isFollowing = true)
        }
        return Response.Success(
            data = GetFollowsResponse(success = true, follows = following)
        )
    }

    override suspend fun getFollowingSuggestions(userId: Long): Response<GetFollowsResponse> {
        val hasFollowing = followDao.getFollowing(userId = userId, pageNumber = 0, pageSize = 1).isNotEmpty()
        return if (hasFollowing){
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = GetFollowsResponse(success = false, message = "User has following")
            )
        }else {
            val suggestedFollowingRows = userDao.getPopularUsers(limit = Constants.SUGGESTED_FOLLOWING_LIMIT)
            val suggestedFollowing = suggestedFollowingRows.filterNot {
                it.id == userId
            }.map {
                toFollowUserData(userRow = it, isFollowing = false)
            }
            return Response.Success(
                data = GetFollowsResponse(success = true, follows = suggestedFollowing)
            )
        }
    }



    private fun toFollowUserData(userRow: UserRow, isFollowing: Boolean): FollowUserData{
        return FollowUserData(
            id = userRow.id,
            name = userRow.name,
            bio = userRow.bio,
            imageUrl = userRow.imageUrl,
            isFollowing = isFollowing
        )
    }
}