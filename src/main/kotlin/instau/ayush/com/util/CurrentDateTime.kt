package instau.ayush.com.util

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.QueryBuilder
import java.time.LocalDateTime

class CurrentDateTime : Expression<LocalDateTime>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) =
        queryBuilder { append("CURRENT_TIMESTAMP AT TIME ZONE 'Asia/Kolkata'") }
}