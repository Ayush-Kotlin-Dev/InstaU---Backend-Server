package instau.ayush.com.route

import com.google.api.RoutingProto.routing
import instau.ayush.com.model.Event
import instau.ayush.com.model.EventParams
import instau.ayush.com.repository.events.EventsRepository
import instau.ayush.com.repository.events.EventsRepositoryImpl
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

            get("/get"){
                val result = eventRepository.getEvents()
                call.respond(
                    result.code,
                    result.data
                )
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