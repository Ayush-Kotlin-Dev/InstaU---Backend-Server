package instau.ayush.com.dao.fcm

import instau.ayush.com.dao.DatabaseFactory.dbQuery
import instau.ayush.com.dao.user.UserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class FcmDaoImpl : FcmDao {
    override suspend fun saveToken(userId: Long, token: String): Boolean {
        val userName = getUserName(userId)
        return if (userName != null) {
            dbQuery {
                val insertStatement = FcmTokenTable.insert {
                    it[FcmTokenTable.userId] = userId
                    it[FcmTokenTable.token] = token
                    it[FcmTokenTable.userName] = userName
                }
                insertStatement.resultedValues?.singleOrNull() != null
            }
        } else {
            false
        }
    }

    override suspend fun getToken(userId: Long): Pair<String, String>? {
        return dbQuery {
            FcmTokenTable.select {
                FcmTokenTable.userId eq userId
            }.singleOrNull()?.let {
                val token = it[FcmTokenTable.token]
                val userName = it[FcmTokenTable.userName]
                Pair(userName, token)
            }
        }
    }

    override suspend fun deleteToken(userId: Long): Boolean {
        return dbQuery {
            FcmTokenTable.deleteWhere {
                FcmTokenTable.userId eq userId
            } > 0
        }
    }
    private suspend fun getUserName(userId: Long): String? {
        return dbQuery {
            UserTable.select {
                UserTable.id eq userId
            }.singleOrNull()?.get(UserTable.name)
        }
    }
}