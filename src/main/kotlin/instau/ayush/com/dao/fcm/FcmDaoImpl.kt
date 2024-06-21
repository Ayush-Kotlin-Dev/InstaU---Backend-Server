package instau.ayush.com.dao.fcm

import instau.ayush.com.dao.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class FcmDaoImpl : FcmDao {
    override suspend fun saveToken(userId: Long, token: String): Boolean {
        return dbQuery {
            val insertStatement = FcmTokenTable.insert {
                it[FcmTokenTable.userId] = userId
                it[FcmTokenTable.token] = token
            }
            insertStatement.resultedValues?.singleOrNull() != null
        }
    }

    override suspend fun getToken(userId: Long): String {
        return dbQuery {
            FcmTokenTable.select {
                FcmTokenTable.userId eq userId
            }.singleOrNull()?.get(FcmTokenTable.token) ?: ""
        }
    }

    override suspend fun deleteToken(userId: Long): Boolean {
        return dbQuery {
            FcmTokenTable.deleteWhere {
                FcmTokenTable.userId eq userId
            } > 0
        }
    }
}