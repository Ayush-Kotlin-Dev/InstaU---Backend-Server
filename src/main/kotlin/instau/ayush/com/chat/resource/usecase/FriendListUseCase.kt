package instau.ayush.com.chat.resource.usecase

import instau.ayush.com.chat.domain.repository.ChatRepository
import instau.ayush.com.chat.resource.FriendListResponseState
import instau.ayush.com.chat.resource.data.toFriendData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FriendListUseCase(private val repository: ChatRepository) {

    suspend operator fun invoke(sender: Long): Flow<FriendListResponseState> = flow {
        repository.getFriendList(sender).collect { friendList ->
            val result = if (friendList.isNotEmpty()) {
                FriendListResponseState(data = friendList.map { friend ->
                    friend.toFriendData()
                }, error = null)
            } else {
                FriendListResponseState(data = emptyList(), error = null)
            }
            emit(result)
        }
    }

}