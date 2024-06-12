package instaU.ayush.com.chat.resource.data

import instaU.ayush.com.chat.domain.model.chatRoom.response.MessageResponseDto
import instaU.ayush.com.chat.domain.model.firendList.FriendDataResponseDto
import instaU.ayush.com.chat.domain.model.firendList.FriendInfo
import instaU.ayush.com.chat.resource.MessageEntity
import instaU.ayush.com.model.chat.User


fun User.toFriendData() = FriendDataResponseDto(
    token = token,
    friendInfo = FriendInfo(
        username = user?.username,
        email = user?.email,
        avatar = user?.avatar,
        lastMessage = user?.lastMessage
    )
)

fun Message.toMessageEntity() = MessageEntity(

    sessionId = sessionId,
    textMessage = textMessage,
    sender = sender,
    receiver = receiver,
    timestamp = timestamp,
)

fun Message.toMessageResponseDto() = MessageResponseDto(
    textMessage = textMessage,
    sender = sender,
    receiver = receiver,
    timestamp = timestamp,
)

fun MessageEntity.toMessage() = Message(
    sessionId = sessionId,
    textMessage = textMessage,
    sender = sender,
    receiver = receiver,
    timestamp = timestamp,
)