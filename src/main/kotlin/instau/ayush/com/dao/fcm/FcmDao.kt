package instau.ayush.com.dao.fcm

interface FcmDao {

    suspend fun saveToken(userId: Long, token: String): Boolean

    suspend fun getToken(senderId : Long ,receiverId: Long): Pair<String, String>?

    suspend fun deleteToken(userId: Long): Boolean

    suspend fun getUserName (userId: Long): String
}