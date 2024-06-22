package instau.ayush.com.repository.fcm

import instau.ayush.com.dao.fcm.FcmDao
import instau.ayush.com.util.Response
import io.ktor.http.*

class FcmRepositoryImpl(
    private val fcmDao: FcmDao
) : FcmRepository {
    override suspend fun saveToken(userId: Long, token: String): Response<Boolean> {
        val tokenIsSaved = fcmDao.saveToken(userId, token)
        if(tokenIsSaved) {
            return Response.Success(true)
        }else{
            return Response.Error(HttpStatusCode.InternalServerError, false)
        }
    }

    override suspend fun getToken(userId: Long): Pair<String, String>? {
        return fcmDao.getToken(userId)
    }
}