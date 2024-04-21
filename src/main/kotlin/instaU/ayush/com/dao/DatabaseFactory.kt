package instaU.ayush.com.dao

import instaU.ayush.com.dao.user.UserRow
import instaU.ayush.com.dao.user.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(){
        Database.connect(createHikariDataSource())
        transaction {
            SchemaUtils.create(UserTable )
        }
    }

    private fun createHikariDataSource(): HikariDataSource{
        val driverClass = "org.postgresql.Driver"
        val jdbcUrl = "jdbc:postgresql://localhost:5432/socialmediadb"

        var username = "postgres" //enter your postgres username
        var password = "Ayush@123" //enter your postgres password

        val hikariConfig = HikariConfig().apply {
            driverClassName = driverClass
            setJdbcUrl(jdbcUrl)
            this.username = username // Set the username here
            this.password = password //
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













