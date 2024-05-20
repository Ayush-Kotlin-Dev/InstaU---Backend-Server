package instaU.ayush.com.route

import instaU.ayush.com.model.ProfileResponse
import instaU.ayush.com.model.UpdateUserParams
import instaU.ayush.com.repository.profile.ProfileRepository
import instaU.ayush.com.util.Constants
import instaU.ayush.com.util.getLongParameter
import instaU.ayush.com.util.saveFile
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.io.File

fun Routing.ProfileRouting() {
    val repository by inject<ProfileRepository>()

    authenticate {
        route(path = "/profile") {
            get(path = "/{userId}") {

                try {
                    val userId = call.getLongParameter("userId")
                    val currentUserId = call.getLongParameter("currentUserId", true)

                    val result = repository.getUserById(userId, currentUserId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (
                    e: BadRequestException
                ) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "An unexpected error occurred , try again "
                    )
                }
            }

            post(path = "/update") {

                val updateUserParams = call.receiveNullable<UpdateUserParams>()

                try {
                    if (updateUserParams == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = ProfileResponse(
                                success = false,
                                message = "Invalid request"
                            )
                        )
                        return@post
                    }

                    val result = repository.updateUser(updateUserParams)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )


                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = ProfileResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }
            // In ProfileRoute.kt
            get("/search") {
                val name = call.request.queryParameters["name"]
                try {
                    if (name == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = "Missing name parameter"
                        )
                        return@get
                    }
                    val users = repository.searchUsersByName(name)
                    call.respond(
                        status = users.code,
                        message = users.data
                    )
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = "An unexpected error has occurred, try again!"
                    )
                }
            }
        }
    }
}