package instaU.ayush.com.repository.chat

import instaU.ayush.com.dao.user.UserRow
import instaU.ayush.com.util.Response
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.ls.LSInput

interface MessageRepository {

    suspend fun getFriendList(userId: Long): Response<Flow<List<UserRow>>>
}