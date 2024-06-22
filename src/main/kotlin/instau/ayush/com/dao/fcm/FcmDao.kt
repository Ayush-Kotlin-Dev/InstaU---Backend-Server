package instau.ayush.com.dao.fcm

interface FcmDao {

    suspend fun saveToken(userId: Long, token: String): Boolean

    suspend fun getToken(userId: Long): Pair<String, String>?

    suspend fun deleteToken(userId: Long): Boolean
}