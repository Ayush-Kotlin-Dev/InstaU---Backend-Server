package instau.ayush.com.dao.events

import instau.ayush.com.dao.DatabaseFactory.dbQuery
import instau.ayush.com.model.Event
import instau.ayush.com.util.IdGenerator
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class EventsDaoImp : EventsDao {
    override suspend fun createEvent(event: Event): Boolean {
        return dbQuery {
            val insertStatement = EventTable.insert {
                it [id] = IdGenerator.generateId()
                it[name] = event.name
                it[description] = event.description
                it[imageUrl] = event.imageUrl
                it[dateTime] = Instant.parse(event.dateTime).atOffset(ZoneOffset.UTC).toLocalDateTime()
                it[organizer] = event.organizer
                it[howToJoin] = event.howToJoin
                it[additionalInfo] = event.additionalInfo
            }
            insertStatement.resultedValues?.singleOrNull() != null
        }
    }

    override suspend fun getEvents(): List<Event> {
        return dbQuery {
            EventTable.selectAll().map { row ->
                Event(
                    id = row[EventTable.id],
                    name = row[EventTable.name],
                    description = row[EventTable.description],
                    imageUrl = row[EventTable.imageUrl],
                    dateTime = row[EventTable.dateTime].toString(),
                    organizer = row[EventTable.organizer],
                    howToJoin = row[EventTable.howToJoin],
                    additionalInfo = row[EventTable.additionalInfo]
                )
            }
        }
    }

    override suspend fun deleteEvent(id: Long): Boolean {
        return dbQuery {
            EventTable.deleteWhere { EventTable.id eq id } > 0
        }
    }


    override suspend fun getEvent(id: Long): Event? {
        TODO("Not yet implemented")
    }
}