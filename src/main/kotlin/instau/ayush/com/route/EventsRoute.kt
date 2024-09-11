package instau.ayush.com.route

import com.google.api.RoutingProto.routing
import instau.ayush.com.model.Event
import instau.ayush.com.model.EventParams
import instau.ayush.com.repository.events.EventsRepository
import instau.ayush.com.repository.events.EventsRepositoryImpl
import instau.ayush.com.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.text.get


fun Routing.EventRouting(){

    val eventRepository by inject<EventsRepository>()

        route("/events") {
            post ("create"){
                val event = call.receiveNullable<EventParams>()
                if (event == null){
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "Invalid event data"
                    )
                    return@post
                }
                val result = eventRepository.createEvent(event)
                call.respond(
                    status = result.code,
                    message = result.data
                )
            }

            get("/get") {
                val pageNumber = call.parameters["page"]?.toIntOrNull() ?: 1
                val pageSize = call.parameters["size"]?.toIntOrNull() ?: 10

                when (val response = eventRepository.getEvents(pageNumber, pageSize)) {
                    is Response.Success -> call.respond(response.data)
                    is Response.Error -> call.respond(response.code, response.data)
                }
            }

            get("/count") {
                when (val response = eventRepository.getTotalEventCount()) {
                    is Response.Success -> call.respond(HttpStatusCode.OK, response.data)
                    is Response.Error -> call.respond(response.code, "Failed to get event count")
                }
            }

            delete("/delete/{id}"){
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null){
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "Invalid event id"
                    )
                    return@delete
                }
                val result = eventRepository.deleteEvent(id)
                call.respond(
                    status = result.code,
                    message = result.data
                )
            }
        }
}