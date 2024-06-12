package instaU.ayush.com.chat.resource.data

import instaU.ayush.com.chat.domain.model.chatRoom.response.MessageResponseDto
import instaU.ayush.com.chat.domain.model.firendList.FriendDataResponseDto
import instaU.ayush.com.chat.domain.model.firendList.FriendInfo
import instaU.ayush.com.chat.resource.MessageEntity
import instaU.ayush.com.model.chat.User


fun User.toFriendData() = FriendDataResponseDto(
    friendInfo = FriendInfo(
        username = user?.username,
        userId = user?.userId,
        email = user?.email,
        avatar = user?.avatar,
        lastMessage = user?.lastMessage
    )
)

fun Message.toMessageEntity() = MessageEntity(
    messageId = messageId,
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
    messageId = messageId,
    sessionId = sessionId,
    textMessage = textMessage,
    sender = sender,
    receiver = receiver,
    timestamp = timestamp,
)