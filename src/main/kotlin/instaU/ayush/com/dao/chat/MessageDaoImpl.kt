package instaU.ayush.com.dao.chat

import instaU.ayush.com.dao.chat.ChatSessionTable.sender
import instaU.ayush.com.dao.user.UserRow
import instaU.ayush.com.dao.user.UserTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


class MessageDaoImpl : MessageDao {

    override suspend fun getFriendList(userId: Long): Flow<List<UserRow>> = flow {
        val friendList = transaction {
            (UserTable innerJoin ChatSessionTable)
                .select { (ChatSessionTable.sender eq userId) or (ChatSessionTable.receiver eq userId) }
                .mapNotNull { row ->
                    val friendId = if (row[ChatSessionTable.sender] == userId) row[ChatSessionTable.receiver] else row[ChatSessionTable.sender]
                    val lastMessage = getLastMessage(userId, friendId)
                    UserRow(
                        id = row[UserTable.id],
                        name = row[UserTable.name],
                        bio = row[UserTable.bio],
                        password = row[UserTable.password],
                        imageUrl = row[UserTable.imageUrl],
                        followersCount = row[UserTable.followersCount],
                        followingCount = row[UserTable.followingCount],
                        lastMessage = lastMessage
                    )
                }
        }
        emit(friendList)
    }

    private fun getLastMessage(sender: Long, receiver: Long): MessageRow? {
        return transaction {
            MessageTable
                .select {
                    ((MessageTable.senderId eq sender) and (MessageTable.receiverId eq receiver)) or
                            ((MessageTable.senderId eq receiver) and (MessageTable.receiverId eq sender))
                }
                .orderBy(MessageTable.timestamp, SortOrder.DESC)
                .firstOrNull()
                ?.let {
                    MessageRow(
                        senderId = it[MessageTable.senderId],
                        receiverId = it[MessageTable.receiverId],
                        timestamp = it[MessageTable.timestamp],
                        content = it[MessageTable.content],
                        sessionId = it[MessageTable.sessionId],
                        id = it[MessageTable.id]
                    )
                }
        }
    }
    private fun rowToUser(row: ResultRow): UserRow {
        return UserRow(
            id = row[UserTable.id],
            name = row[UserTable.name],
            bio = row[UserTable.bio],
            password = row[UserTable.password],
            imageUrl = row[UserTable.imageUrl],
            followersCount = row[UserTable.followersCount],
            followingCount = row[UserTable.followingCount]
        )
    }
}
