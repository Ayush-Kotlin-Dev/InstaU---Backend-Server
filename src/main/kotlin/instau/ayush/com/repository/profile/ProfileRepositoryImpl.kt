package instau.ayush.com.repository.profile

import instau.ayush.com.dao.follows.FollowsDao
import instau.ayush.com.dao.user.UserDao
import instau.ayush.com.dao.user.UserRow
import instau.ayush.com.model.*
import instau.ayush.com.util.Response
import io.ktor.http.*

class ProfileRepositoryImpl(
    private val userDao: UserDao,
    private val followsDao: FollowsDao
) : ProfileRepository {
    override suspend fun getUserById(userId: Long, currentUserId: Long): Response<ProfileResponse> {
        val userRow = userDao.findById(userId = userId)

        return if (userRow == null) {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = ProfileResponse(success = false, message = "Could not find user with id: $userId")
            )
        } else {
            val isFollowing = followsDao.isAlreadyFollowing(follower = currentUserId, following = userId)
            val isOwnProfile = userId == currentUserId

            Response.Success(
                data = ProfileResponse(success = true, profile = toProfile(userRow, isFollowing, isOwnProfile))
            )
        }
    }

    override suspend fun updateUser(updateUserParams: UpdateUserParams): Response<ProfileResponse> {
        val userExists = userDao.findById(userId = updateUserParams.userId) != null

        if (userExists) {
            val userUpdated = userDao.updateUser(
                userId = updateUserParams.userId,
                name = updateUserParams.name,
                bio = updateUserParams.bio,
                imageUrl = updateUserParams.imageUrl
            )

            return if (userUpdated) {
                Response.Success(
                    data = ProfileResponse(success = true)
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Conflict,
                    data = ProfileResponse(
                        success = false,
                        message = "Could not update user: ${updateUserParams.userId}"
                    )
                )
            }
        } else {
            return Response.Error(
                code = HttpStatusCode.NotFound,
                data = ProfileResponse(success = false, message = "Could not find user: ${updateUserParams.userId}")
            )
        }
    }

    override suspend fun searchUsersByName(name: String): Response<GetFollowsResponse> {
        val usersRows = userDao.searchUsersByName(name = name)
        val users = usersRows.map {
            toFollowUserData(userRow = it, isFollowing = false)
        }
        return if(users.isEmpty()){
             Response.Error(
                code = HttpStatusCode.NotFound,
                data = GetFollowsResponse(success = false, message = "No users found with name: $name")
            )
        }else{
            return Response.Success(data = GetFollowsResponse(success = true, follows = users))
        }
    }

    private fun toProfile(userRow: UserRow, isFollowing: Boolean, isOwnProfile: Boolean): Profile{
        return Profile(
            id = userRow.id,
            name = userRow.name,
            bio = userRow.bio,
            imageUrl = userRow.imageUrl,
            followersCount = userRow.followersCount,
            followingCount = userRow.followingCount,
            isFollowing = isFollowing,
            isOwnProfile = isOwnProfile
        )
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