package instaU.ayush.com.chat

import instaU.ayush.com.chat.domain.repository.ChatRepository
import instaU.ayush.com.chat.resource.ChatRoomHistoryState
import instaU.ayush.com.chat.resource.data.toMessageResponseDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetHistoryMessagesUseCase(private val repository: ChatRepository) {

    suspend operator fun invoke(sender : String , receiver: String): Flow<ChatRoomHistoryState> = flow {

        repository.getHistoryMessages(sender = sender, receiver = receiver)
            .collect { messageList ->
                val result = if (messageList.isNotEmpty()) {
                    ChatRoomHistoryState(data = messageList.map { it.toMessageResponseDto() }, error = null)
                } else {
                    ChatRoomHistoryState(data = emptyList(), error = null)
                }
                emit(result)
            }
    }
}