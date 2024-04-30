package instaU.ayush.com.dao

import instaU.ayush.com.dao.user.UserRow
import instaU.ayush.com.dao.user.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import instaU.ayush.com.dao.follows.FollowsTable
import instaU.ayush.com.dao.post.PostTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {
    fun init(){
        Database.connect(createHikariDataSource())
        transaction {
            SchemaUtils.create(UserTable  , FollowsTable , PostTable)
        }
    }

    private fun createHikariDataSource(): HikariDataSource{
        val driverClass = "org.postgresql.Driver"
        val databaseUri = URI("postgres://ayush:4Fq3ORjBGLxpCIWOvc2pTvxcM61CMZmd@dpg-coj1vqdjm4es73a0trkg-a.oregon-postgres.render.com/socialmediadb_kb67")
        val port = if (databaseUri.port != -1) databaseUri.port else 5432
        val jdbcUrl = "jdbc:postgresql://" + databaseUri.host + ':' + port + databaseUri.path

        val userInfo = databaseUri.userInfo.split(":")
        val username = userInfo[0]
        val password = userInfo[1]

        val hikariConfig = HikariConfig().apply {
            driverClassName = driverClass
            setJdbcUrl(jdbcUrl)
            this.username = username
            this.password = password
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        return HikariDataSource(hikariConfig)
    }

    //Generic Function to handle db transactions
    suspend fun <T> dbQuery(block: suspend () -> T) =
        newSuspendedTransaction(Dispatchers.IO) { block()  }
}