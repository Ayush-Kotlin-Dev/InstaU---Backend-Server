package instaU.ayush.com.chat.resource.usecase

import instaU.ayush.com.chat.domain.repository.ChatRepository
import instaU.ayush.com.chat.resource.FriendListResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FriendListUseCase(private val repository: ChatRepository) {

    suspend operator fun invoke(): Flow<FriendListResponseState> = flow {
        repository.getFriendList().collect { friendList ->
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