package instau.ayush.com.route

import instau.ayush.com.model.CommentResponse
import instau.ayush.com.model.NewCommentParams
import instau.ayush.com.model.RemoveCommentParams
import instau.ayush.com.repository.PostComments.PostCommentsRepository
import instau.ayush.com.util.Constants.DEFAULT_PAGE_SIZE
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.postCommentsRouting() {
    val repository by inject<PostCommentsRepository>()

    authenticate {
        route("/post/comments") {
            post("/create") {
                try {
                    val params = call.receiveNullable<NewCommentParams>()
                    if (params == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = CommentResponse(
                                success = false,
                                message = "Invalid request"
                            )
                        )
                        return@post
                    }

                    val result = repository.addComment(
                        params = params,
                    )
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: Exception) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = CommentResponse(
                            success = false,
                            message = "Oops, something went wrong!"
                        )
                    )
                }
            }

            delete("/delete") {
                try {
                    val params = call.receiveNullable<RemoveCommentParams>()
                    if (params == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = CommentResponse(
                                success = false,
                                message = "Invalid request"
                            )
                        )
                        return@delete
                    }
                    val result = repository.removeComment(
                        params = params,
                    )
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: Exception) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = CommentResponse(
                            success = false,
                            message = "Oops, something went wrong!"
                        )
                    )
                }
            }

            get("/{postId}") {
                try {
                    val postId = call.parameters["postId"]?.toLongOrNull()
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 8
                    val limit  = call.request.queryParameters["limit"]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE

                    if (postId == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = CommentResponse(
                                success = false,
                                message = "Invalid request"
                            )
                        )
                        return@get
                    }
                    val result = repository.getComments(
                        postId = postId,
                        pageNumber = page,
                        pageSize = limit
                    )
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: Exception) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = CommentResponse(
                            success = false,
                            message = "Oops, something went wrong!"
                        )
                    )
                }
            }
        }
    }
}