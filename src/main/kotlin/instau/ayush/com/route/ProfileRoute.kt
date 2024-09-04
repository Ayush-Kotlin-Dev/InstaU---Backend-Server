package instau.ayush.com.route

import com.google.firebase.cloud.StorageClient
import instau.ayush.com.model.PostParams
import instau.ayush.com.model.ProfileResponse
import instau.ayush.com.model.UpdateUserParams
import instau.ayush.com.model.UpdateUserText
import instau.ayush.com.repository.profile.ProfileRepository
import instau.ayush.com.util.getLongParameter
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
import java.util.*

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
                val multipart = call.receiveMultipart()
                var updateText: UpdateUserText? = null
                var fileExtension: String? = null
                var fileBytes: ByteArray? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            fileExtension = part.originalFileName?.substringAfterLast('.', "")
                            fileBytes = part.streamProvider().readBytes()
                        }

                        is PartData.FormItem -> {
                            if (part.name == "profile_data") {
                                updateText = Json.decodeFromString(part.value)
                            }
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                if (fileExtension != null && fileBytes != null && updateText != null) {
                    try {
                        val bucket = StorageClient.getInstance().bucket()

                        // Generate a unique filename
                        val uniqueFileName = "${UUID.randomUUID()}.${fileExtension}"

                        val blob = bucket.create("profile_images/$uniqueFileName", fileBytes)

                        // Generate a UUID as a token
                        val token = UUID.randomUUID().toString()

                        // Set the token as metadata
                        blob.toBuilder().setMetadata(mapOf("firebaseStorageDownloadTokens" to token)).build().update()

                        // Construct the public URL with token
                        val publicUrl =
                            "https://firebasestorage.googleapis.com/v0/b/${bucket.name}/o/profile_images%2F$uniqueFileName?alt=media&token=$token"

                        val result = repository.updateUser(
                            UpdateUserParams(
                                userId = updateText!!.userId,
                                name = updateText!!.name,
                                bio = updateText!!.bio,
                                imageUrl = publicUrl
                            )
                        )
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
                }else {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = ProfileResponse(
                            success = false,
                            message = "File upload failed $fileExtension $fileBytes $updateText"
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