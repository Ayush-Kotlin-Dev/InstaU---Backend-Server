package instaU.ayush.com.dao.chat

import instaU.ayush.com.dao.user.UserRow
import instaU.ayush.com.dao.user.UserTable
import instaU.ayush.com.model.ChatMessage
import instaU.ayush.com.route.User
import kotlinx.coroutines.flow.Flow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

interface MessageDao {
    suspend fun getFriendList(userId: Long): Flow<List<UserRow>>

}