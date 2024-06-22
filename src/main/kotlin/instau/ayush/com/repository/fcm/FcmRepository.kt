package instau.ayush.com.repository.fcm

import instau.ayush.com.util.Response

interface FcmRepository {

    suspend fun saveToken(userId: Long, token: String) : Response<Boolean>
    suspend fun getToken(senderId : Long ,receiverId: Long): Pair<String, String>?
}