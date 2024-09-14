package instau.ayush.com.repository.events

import instau.ayush.com.dao.events.EventsDao
import instau.ayush.com.model.Event
import instau.ayush.com.model.EventParams
import instau.ayush.com.model.EventResponse
import instau.ayush.com.model.EventsResponse
import instau.ayush.com.util.Response
import io.ktor.http.*

class EventsRepositoryImpl(
    private val eventsDao: EventsDao
) : EventsRepository {
    override suspend fun createEvent(eventsParams: EventParams): Response<EventResponse> {
        val isCreated = eventsDao.createEvent(
            Event(
                name = eventsParams.name,
                description = eventsParams.description,
                imageUrl = eventsParams.imageUrl,
                dateTime = eventsParams.dateTime,
                organizer = eventsParams.organizer,
                howToJoin = eventsParams.howToJoin,
                additionalInfo = eventsParams.additionalInfo ,
                location = eventsParams.location
            )
        )
        return if (isCreated) {
            Response.Success(
                EventResponse(
                    true,
                    message = "Event created successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = EventResponse(
                    false,
                    message = "Failed to create event"
                )
            )
        }
    }

    override suspend fun getEvents(pageNumber: Int, pageSize: Int): Response<EventsResponse> {
        val events = eventsDao.getEvents(pageNumber, pageSize)
        return Response.Success(
            EventsResponse(
                true,
                events = events
            )
        )
    }

    override suspend fun getEvent(id: Long): Response<EventResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(id: Long): Response<EventResponse> {
        val isDeleted = eventsDao.deleteEvent(id)
        return if (isDeleted) {
            Response.Success(
                EventResponse(
                    true,
                    message = "Event deleted successfully"
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = EventResponse(
                    false,
                    message = "Failed to delete event"
                )
            )
        }
    }

    override suspend fun getTotalEventCount(): Response<Long> {
        val count = eventsDao.getTotalEventCount()
        return Response.Success(count)
    }
}