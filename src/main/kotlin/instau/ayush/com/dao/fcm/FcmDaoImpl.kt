package instau.ayush.com.dao.fcm

import instau.ayush.com.dao.DatabaseFactory.dbQuery
import instau.ayush.com.dao.user.UserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.ConcurrentHashMap

class FcmDaoImpl : FcmDao {
    private val userCache = ConcurrentHashMap<Long, Pair<String, String>>()

    override suspend fun saveToken(userId: Long, token: String): Boolean {
        val userName = getUserName(userId)
        return if (userName != null) {
            dbQuery {
                val insertStatement = FcmTokenTable.insert {
                    it[FcmTokenTable.userId] = userId
                    it[FcmTokenTable.token] = token
                    it[FcmTokenTable.userName] = userName
                }
                val result = insertStatement.resultedValues?.singleOrNull() != null
                if (result) {
                    userCache[userId] = Pair(userName, token)
                }
                result
            }
        } else {
            false
        }
    }
    // TODO have access to senders token also
    override suspend fun getToken(senderId: Long, receiverId: Long): Pair<String, String>? = coroutineScope {
        val receiverDataDeferred = async {
            userCache[receiverId]?.takeIf { it.second.isNotEmpty() } ?: dbQuery {
                FcmTokenTable.select {
                    FcmTokenTable.userId eq receiverId
                }.singleOrNull()?.let {
                    val token = it[FcmTokenTable.token]
                    val userName = it[FcmTokenTable.userName]
                    userCache[receiverId] = Pair(userName, token)
                    Pair(userName, token)
                }
            }
        }

        val senderNameDeferred = async {
            userCache[senderId]?.first ?: dbQuery {
                UserTable.select {
                    UserTable.id eq senderId
                }.singleOrNull()?.get(UserTable.name)?.also { senderName ->
                    userCache[senderId] = Pair(senderName, userCache[senderId]?.second ?: "")
                    println("Cache updated for sender: $senderId with name: $senderName")
                } ?: run {
                    println("No name found for sender: $senderId in the database.")
                    null
                }
            }
        }

        val receiverData = receiverDataDeferred.await()
        val senderName = senderNameDeferred.await()

        if (receiverData != null && senderName != null) {
            Pair(senderName, receiverData.second)
        } else {
            println("Failed to retrieve tokens: senderName=$senderName, receiverData=$receiverData")
            null
        }
    }

    override suspend fun deleteToken(userId: Long): Boolean {
        userCache.remove(userId)
        return dbQuery {
            FcmTokenTable.deleteWhere {
                FcmTokenTable.userId eq userId
            } > 0
        }
    }

    override suspend fun getUserName(userId: Long): String {
        return dbQuery {
            UserTable.select {
                UserTable.id eq userId
            }.singleOrNull()?.get(UserTable.name) ?: ""
        }
    }
}
