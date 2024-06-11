package instaU.ayush.com.chat.data.source

import instaU.ayush.com.chat.resource.MessageEntity
import instaU.ayush.com.model.chat.UserEntity
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {
    suspend fun getFriendList(sender: String): Flow<List<UserEntity>>
    suspend fun checkSessionAvailability(sender: String, receiver: String): String?
    suspend fun createNewSession(sender: String, receiver: String): String
    suspend fun insertMessage(messageEntity: MessageEntity)
    suspend fun getHistoryMessages(sender: String, receiver: String): Flow<List<MessageEntity>>
}