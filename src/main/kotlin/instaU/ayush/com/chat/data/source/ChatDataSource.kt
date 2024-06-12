package instaU.ayush.com.chat.data.source

import instaU.ayush.com.chat.resource.MessageEntity
import instaU.ayush.com.model.chat.UserEntity
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {
    suspend fun getFriendList(sender: String): Flow<List<UserEntity>>
    suspend fun checkSessionAvailability(sender: Long, receiver: Long): Long?
    suspend fun createNewSession(sender: Long, receiver: Long): Long
    suspend fun insertMessage(messageEntity: MessageEntity)
    suspend fun getHistoryMessages(sender: String, receiver: String): Flow<List<MessageEntity>>
}