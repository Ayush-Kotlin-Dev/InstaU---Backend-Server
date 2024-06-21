package instau.ayush.com.dao

import instau.ayush.com.dao.user.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import instau.ayush.com.dao.PostComment.PostCommentTable
import instau.ayush.com.dao.chat.ChatSessionTable
import instau.ayush.com.dao.chat.MessageTable
import instau.ayush.com.dao.fcm.FcmTokenTable
import instau.ayush.com.dao.follows.FollowsTable
import instau.ayush.com.dao.post.PostTable
import instau.ayush.com.dao.postlikes.PostLikesTable
import instau.ayush.com.dao.qna.AnswersTable
import instau.ayush.com.dao.qna.QuestionsTable
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
            SchemaUtils.create(
                UserTable,
                FollowsTable ,
                PostTable ,
                PostCommentTable ,
                PostLikesTable ,
                MessageTable,
                ChatSessionTable,
                QuestionsTable,
                AnswersTable,
                FcmTokenTable
            )
        }
    }

    private fun createHikariDataSource(): HikariDataSource{
        val driverClass = "org.postgresql.Driver"
        val databaseUri = URI(System.getenv("DATABASE_URL"))

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