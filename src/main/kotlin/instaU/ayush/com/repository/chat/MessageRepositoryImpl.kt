package instaU.ayush.com.repository.chat

import instaU.ayush.com.dao.chat.MessageDao
import instaU.ayush.com.dao.user.UserRow
import instaU.ayush.com.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val messageDao: MessageDao
) : MessageRepository {

    override suspend fun getFriendList(userId: Long): Response<Flow<UserRow>> {

        val friendList = messageDao.getFriendList(userId)
        return Response.Success(

        )
    }
}