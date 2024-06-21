package instau.ayush.com.chat.resource.data

import instau.ayush.com.chat.domain.model.chatRoom.response.MessageResponseDto
import instau.ayush.com.chat.domain.model.firendList.FriendDataResponseDto
import instau.ayush.com.chat.domain.model.firendList.FriendInfo
import instau.ayush.com.chat.resource.MessageEntity
import instau.ayush.com.model.chat.User


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