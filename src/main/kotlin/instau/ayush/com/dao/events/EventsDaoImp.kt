package instau.ayush.com.dao.events

import instau.ayush.com.dao.DatabaseFactory.dbQuery
import instau.ayush.com.model.Event
import instau.ayush.com.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.Instant
import java.time.ZoneOffset

class EventsDaoImp : EventsDao {
    override suspend fun createEvent(event: Event): Boolean {
        return dbQuery {
            val insertStatement = EventTable.insert {
                it[id] = IdGenerator.generateId()
                it[name] = event.name
                it[description] = event.description
                it[imageUrl] = event.imageUrl
                it[dateTime] = Instant.parse(event.dateTime).atOffset(ZoneOffset.UTC).toLocalDateTime()
                it[organizer] = event.organizer
                it[howToJoin] = event.howToJoin
                it[additionalInfo] = event.additionalInfo
                it[location] = event.location

            }
            insertStatement.resultedValues?.singleOrNull() != null
        }
    }

    override suspend fun getEvents(pageNumber: Int, pageSize: Int): List<Event> {
        return dbQuery {
            EventTable
                .selectAll()
                .orderBy(EventTable.dateTime, SortOrder.DESC)
                .limit(n = pageSize, offset = ((pageNumber - 1) * pageSize).toLong())
                .map { row ->
                    Event(
                        id = row[EventTable.id],
                        name = row[EventTable.name],
                        description = row[EventTable.description],
                        imageUrl = row[EventTable.imageUrl],
                        dateTime = row[EventTable.dateTime].toString(),
                        organizer = row[EventTable.organizer],
                        howToJoin = row[EventTable.howToJoin],
                        additionalInfo = row[EventTable.additionalInfo],
                        location = row[EventTable.location]
                    )
                }
        }
    }

    override suspend fun deleteEvent(id: Long): Boolean {
        return dbQuery {
            EventTable.deleteWhere { EventTable.id eq id } > 0
        }
    }

    override suspend fun getTotalEventCount(): Long {
        return dbQuery {
            EventTable.selectAll().count()
        }
    }

    override suspend fun getEvent(id: Long): Event? {
        return dbQuery {
            EventTable
                .select { EventTable.id eq id }
                .map { row ->
                    Event(
                        id = row[EventTable.id],
                        name = row[EventTable.name],
                        description = row[EventTable.description],
                        imageUrl = row[EventTable.imageUrl],
                        dateTime = row[EventTable.dateTime].toString(),
                        organizer = row[EventTable.organizer],
                        howToJoin = row[EventTable.howToJoin],
                        additionalInfo = row[EventTable.additionalInfo],
                        location = row[EventTable.location]
                    )
                }
                .singleOrNull()
        }
    }


}