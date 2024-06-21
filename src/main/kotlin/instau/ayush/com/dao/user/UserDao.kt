package instau.ayush.com.dao.user

import instau.ayush.com.model.SignUpParams

interface UserDao {
    suspend fun insert(params: SignUpParams): UserRow?

    suspend fun findByEmail(email: String): UserRow?

    //12
    suspend fun findById(userId: Long): UserRow?

    suspend fun updateUser(userId: Long, name: String, bio: String, imageUrl: String?): Boolean
    //12
    suspend fun updateFollowsCount(follower: Long, following: Long, isFollowing: Boolean): Boolean

    suspend fun getUsers(ids: List<Long>): List<UserRow>

    suspend fun getPopularUsers(limit: Int): List<UserRow>

    suspend fun searchUsersByName(name: String): List<UserRow>
}