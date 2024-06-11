package instaU.ayush.com.repository.chat

import instaU.ayush.com.model.FriendListResponse
import instaU.ayush.com.util.Response
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun getFriendList(userId: Long): Response<Flow<Use>>
}