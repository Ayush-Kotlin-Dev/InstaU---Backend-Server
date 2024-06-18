package instaU.ayush.com.route

import instaU.ayush.com.model.PostTextParams
import instaU.ayush.com.model.qna.QnaTextParams
import instaU.ayush.com.repository.qna.QnaRepository
import instaU.ayush.com.util.Constants
import instaU.ayush.com.util.getLongParameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.QnaRouting() {
    val repository by inject<QnaRepository>()
    authenticate {
        route(path = "/qna") {
            post(path = "/create") {
                try {
                    val qnaTextParams = call.receiveNullable<QnaTextParams>()

                    if(qnaTextParams == null){
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = "Invalid request"
                        )
                        return@post
                    }
                    val result = repository.createQuestion(qnaTextParams)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "An unexpected error occurred , try again "
                    )
                }

            }
            get(path = "/questions") {
                try {
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE
                    val result = repository.getQuestions(page, limit)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "An unexpected error occurred , try again "
                    )
                }
            }
            get(path = "/question/{questionId}") {
                try {
                    val questionId = call.getLongParameter("questionId")
                    val result = repository.getQuestion(questionId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "An unexpected error occurred , try again "
                    )
                }
            }
            delete(path = "/question/{questionId}") {
                try {
                    val questionId = call.getLongParameter("questionId")
                    val result = repository.deleteQuestion(questionId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "An unexpected error occurred , try again "
                    )
                }
            }

            post(path = "/answer/create") {
                try {
                    val qnaTextParams = call.receiveNullable<QnaTextParams>()

                    if(qnaTextParams == null){
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = "Invalid request"
                        )
                        return@post
                    }
                    val result = repository.createQuestion(qnaTextParams)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "An unexpected error occurred , try again "
                    )
                }
            }

            get(path = "/answers") {
                try {
                    val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE
                    val result = repository.getQuestions(page, limit)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "An unexpected error occurred , try again "
                    )
                }
            }

            get(path = "/answer/{answerId}") {
                try {
                    val questionId = call.getLongParameter("answerId")
                    val result = repository.getQuestion(questionId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "An unexpected error occurred , try again "
                    )
                }
            }

            delete(path = "/answer/{answerId}") {
                try {
                    val questionId = call.getLongParameter("answerId")
                    val result = repository.deleteQuestion(questionId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (e: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "An unexpected error occurred , try again "
                    )
                }
            }
        }
    }
}