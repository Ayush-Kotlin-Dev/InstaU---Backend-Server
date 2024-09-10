package instau.ayush.com.dao.events

import instau.ayush.com.model.Event

interface EventsDao {

    suspend fun createEvent(event : Event) : Boolean

    suspend fun getEvents() : List<Event>

    suspend fun getEvent(id : Long) : Event?

    suspend fun deleteEvent(id : Long) : Boolean
}