package instau.ayush.com.chat.resource

import instau.ayush.com.chat.domain.model.firendList.FriendDataResponseDto


@kotlinx.serialization.Serializable
data class FriendListResponseState(
    val data: List<FriendDataResponseDto>? = null,
    val error: HashMap<String, String>? = null
)
