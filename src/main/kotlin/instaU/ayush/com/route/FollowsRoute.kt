package instaU.ayush.com.route

import instaU.ayush.com.model.FollowsAndUnfollowsResponse
import instaU.ayush.com.model.FollowsParams
import instaU.ayush.com.repository.follows.FollowRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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
                        message = FollowsAndUnfollowsResponse(
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
        }
    }
}