package instaU.ayush.com.repository.profile

import instaU.ayush.com.dao.user.UserRow
import instaU.ayush.com.model.ProfileResponse
import instaU.ayush.com.model.UpdateUserParams
import instaU.ayush.com.util.Response

interface ProfileRepository {

    suspend fun getUserById(userId: Long , currentUserId : Long ): Response<ProfileResponse>

    suspend fun updateUser(updateUserParams : UpdateUserParams): Response<ProfileResponse>

}