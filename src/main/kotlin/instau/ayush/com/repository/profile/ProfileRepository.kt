package instau.ayush.com.repository.profile

import instau.ayush.com.model.GetFollowsResponse
import instau.ayush.com.model.ProfileResponse
import instau.ayush.com.model.UpdateUserParams
import instau.ayush.com.util.Response

interface ProfileRepository {

    suspend fun getUserById(userId: Long , currentUserId : Long ): Response<ProfileResponse>

    suspend fun updateUser(updateUserParams : UpdateUserParams): Response<ProfileResponse>

    suspend fun searchUsersByName(name: String): Response<GetFollowsResponse>

}