package instau.ayush.com.repository.events

import instau.ayush.com.model.Event
import instau.ayush.com.model.EventParams
import instau.ayush.com.model.EventResponse
import instau.ayush.com.model.EventsResponse
import instau.ayush.com.util.Response

interface EventsRepository {

    suspend fun createEvent(eventsParams: EventParams) : Response<EventResponse>

    suspend fun getEvents() : Response<EventsResponse>

    suspend fun getEvent(id : Long) : Response<EventResponse>

    suspend fun deleteEvent(id : Long) : Response<EventResponse>
}