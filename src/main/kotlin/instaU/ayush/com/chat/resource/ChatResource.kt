package instaU.ayush.com.chat.resource

import instaU.ayush.com.chat.GetHistoryMessagesUseCase
import instaU.ayush.com.chat.resource.data.ConnectToSocketUseCase
import instaU.ayush.com.chat.resource.usecase.FriendListUseCase
import instaU.ayush.com.util.Constants.ENDPOINT_CHAT_CONNECT
import instaU.ayush.com.util.Constants.ENDPOINT_CHAT_HISTORY
import instaU.ayush.com.util.Constants.ENDPOINT_FRIEND_LIST
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

import org.koin.java.KoinJavaComponent.inject

fun Route.friendsListEndpoint() {
    val useCase by inject<FriendListUseCase>(FriendListUseCase::class.java)
    get(ENDPOINT_FRIEND_LIST) {
        useCase().collect { response ->
            call.respond(response)
        }
    }
}

fun Route.chatHistoryEndpoint() {
    val useCase by inject<GetHistoryMessagesUseCase>(GetHistoryMessagesUseCase::class.java)
    get(ENDPOINT_CHAT_HISTORY) {
        val receiver = call.parameters["receiver"].toString()
        useCase(receiver = receiver).collect { response ->
            call.respond(response)
        }
    }
}

fun Route.chatConnectEndpoint() {
    val useCase by inject<ConnectToSocketUseCase>(ConnectToSocketUseCase::class.java)
    webSocket(ENDPOINT_CHAT_CONNECT) {
        useCase(this)
    }
}