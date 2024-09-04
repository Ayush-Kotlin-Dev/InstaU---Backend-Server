package instau.ayush.com.route

import com.google.firebase.cloud.StorageClient
import instau.ayush.com.model.PostParams
import instau.ayush.com.model.PostResponse
import instau.ayush.com.model.PostTextParams
import instau.ayush.com.repository.post.PostRepository
import instau.ayush.com.repository.post.connectedClients
import instau.ayush.com.util.Constants
import instau.ayush.com.util.getLongParameter
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.util.*

fun Routing.postRouting() {

    val postRepository by inject<PostRepository>()

    authenticate {
        route(path = "/post") {
            post("/create") {
                val multipart = call.receiveMultipart()
                var postTextParams: PostParams? = null
                var fileExtension: String? = null
                var fileBytes: ByteArray? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            fileExtension = part.originalFileName?.substringAfterLast('.', "")
                            fileBytes = part.streamProvider().readBytes()
                        }
                        is PartData.FormItem -> {
                            if (part.name == "post_data") {
                                postTextParams = Json.decodeFromString(part.value)
                            }
                        }
                        else -> {}
                    }
                    part.dispose()
                }

                if (fileExtension != null && fileBytes != null && postTextParams != null) {
                    try {
                        val bucket = StorageClient.getInstance().bucket()

                        // Generate a unique filename
                        val uniqueFileName = "${UUID.randomUUID()}.${fileExtension}"

                        val blob = bucket.create("post_images/$uniqueFileName", fileBytes)

                        // Generate a UUID as a token
                        val token = UUID.randomUUID().toString()

                        // Set the token as metadata
                        blob.toBuilder().setMetadata(mapOf("firebaseStorageDownloadTokens" to token)).build().update()

                        // Construct the public URL with token
                        val publicUrl = "https://firebasestorage.googleapis.com/v0/b/${bucket.name}/o/uploads%2F$uniqueFileName?alt=media&token=$token"

                        val result = postRepository.createPost(
                            PostTextParams(
                                caption = postTextParams!!.caption,
                                userId = postTextParams!!.userId,
                                imageUrl = publicUrl
                            )
                        )
                        call.respond(result.code, message = result.data)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, "Failed to upload file")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "File upload failed")
                }
            }

            get(path = "/{postId}") {
                try {
                    val postId = call.getLongParameter("postId")
                    val currentUserId = call.getLongParameter("currentUserId", true)

                    val result = postRepository.getPost(postId, currentUserId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    return@get
                } catch (e: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = PostResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }

            delete(path = "/{postId}") {
                try {
                    val postId = call.getLongParameter("postId")
                    val result = postRepository.deletePost(postId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    return@delete
                } catch (e: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = PostResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }

        }

        route(path = "/posts") {
            get(path = "/feed") {
                try {
                    val currentUserId = call.getLongParameter("currentUserId", true)
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE
                    val result = postRepository.getFeedsPost(currentUserId, page, limit)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    return@get
                } catch (e: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = PostResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }

            get(path = "/{userId}") {
                try {
                    val postOwnerId = call.getLongParameter("userId")
                    val currentUserId = call.getLongParameter("currentUserId", true)
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE
                    val result = postRepository.getPostByUser(
                        postOwnerId = postOwnerId,
                        currentUserId = currentUserId,
                        pageNumber = page,
                        pageSize = limit
                    )
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    return@get
                } catch (e: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = PostResponse(
                            success = false,
                            message = "An unexpected error has occurred, try again!"
                        )
                    )
                }
            }
        }


    }
}

fun Route.ChangeInPost() {
    webSocket("/ws/posts") {
        connectedClients += this
        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                    // Handle received messages if necessary
                }
            }
        } finally {
            connectedClients -= this
        }
    }
}