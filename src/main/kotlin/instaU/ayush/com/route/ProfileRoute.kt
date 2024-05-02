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
                var fileName = ""
                var updateUserParams: UpdateUserParams? = null
                val multiPartData = call.receiveMultipart()

                try {
                    multiPartData.forEachPart { partData ->
                        when (partData) {
                            is PartData.FileItem -> {
                                fileName = partData.saveFile(folderPath = Constants.PROFILE_IMAGES_FOLDER_PATH)
                            }
                            is PartData.FormItem -> {
                                if (partData.name == "profile_data") {
                                    updateUserParams = Json.decodeFromString(partData.value)
                                }
                            }
                            else -> {
                            }
                        }
                        partData.dispose()
                    }

                    val imageUrl = "${Constants.BASE_URL}${Constants.PROFILE_IMAGES_FOLDER}$fileName"

                    val result = repository.updateUser(
                        updateUserParams!!.copy(
                            imageUrl = if(fileName.isNotEmpty()) imageUrl else updateUserParams!!.imageUrl
                        )
                    )
                    call.respond(
                        result.code,
                        message = result.data
                    )

                } catch (anyError: Throwable) {
                   if(fileName.isNotEmpty()){
                       File("${Constants.PROFILE_IMAGES_FOLDER_PATH}/$fileName").delete()
                   }
                    println(anyError.message) // Log the exception message
                    anyError.printStackTrace()

                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = ProfileResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }
        }
    }
}