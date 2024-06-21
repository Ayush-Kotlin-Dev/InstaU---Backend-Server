package instau.ayush.com.route

import instau.ayush.com.model.FollowAndUnfollowResponse
import instau.ayush.com.model.FollowsParams
import instau.ayush.com.repository.follows.FollowRepository
import instau.ayush.com.util.Constants
import instau.ayush.com.util.getLongParameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Route.followsRouting() {
   val repository by inject <FollowRepository>()

    authenticate {
        route(path = "/follow"){
            post {
                val params = call.receiveNullable<FollowsParams>()
                if (params == null) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = FollowAndUnfollowResponse(
                            success = false,
                            message = "Invalid request , try again"
                        )
                    )
                    return@post
                }

                val result = if (params.isFollowing) {
                    repository.unFollowUser(params.follower, params.following)
                } else {
                    repository.followUser(params.follower, params.following)
                }

                call.respond(
                    status = result.code,
                    message = result.data
                )
            }

            get(path = "/followers") {
                try {
                    val userId = call.getLongParameter(name = Constants.USER_ID_PARAMETER, isQueryParameter = true)
                    val page = call.request.queryParameters[Constants.PAGE_NUMBER_PARAMETER]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters[Constants.PAGE_LIMIT_PARAMETER]?.toIntOrNull()
                        ?: Constants.DEFAULT_PAGE_SIZE

                    val result = repository.getFollowers(userId = userId, pageNumber = page, pageSize = limit)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )

                } catch (badRequestError: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Constants.MISSING_PARAMETERS_ERROR_MESSAGE
                    )
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = Constants.UNEXPECTED_ERROR_MESSAGE
                    )
                }
            }

            get(path = "/following") {
                try {
                    val userId = call.getLongParameter(name = Constants.USER_ID_PARAMETER, isQueryParameter = true)
                    val page = call.request.queryParameters[Constants.PAGE_NUMBER_PARAMETER]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters[Constants.PAGE_LIMIT_PARAMETER]?.toIntOrNull()
                        ?: Constants.DEFAULT_PAGE_SIZE

                    val result = repository.getFollowing(userId = userId, pageNumber = page, pageSize = limit)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (badRequestException: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Constants.MISSING_PARAMETERS_ERROR_MESSAGE
                    )
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = Constants.UNEXPECTED_ERROR_MESSAGE
                    )
                }
            }

            get(path = "/suggestions") {
                try {
                    val userId = call.getLongParameter(name = Constants.USER_ID_PARAMETER, isQueryParameter = true)
                    val result = repository.getFollowingSuggestions(userId = userId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (badRequestError: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Constants.MISSING_PARAMETERS_ERROR_MESSAGE
                    )
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = Constants.UNEXPECTED_ERROR_MESSAGE
                    )
                }
            }
        }
    }
}