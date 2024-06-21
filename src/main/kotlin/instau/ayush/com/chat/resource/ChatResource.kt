package instau.ayush.com.chat.resource

import instau.ayush.com.chat.GetHistoryMessagesUseCase
import instau.ayush.com.chat.resource.data.ConnectToSocketUseCase
import instau.ayush.com.chat.resource.usecase.FriendListUseCase
import instau.ayush.com.util.Constants.ENDPOINT_CHAT_CONNECT
import instau.ayush.com.util.Constants.ENDPOINT_CHAT_HISTORY
import instau.ayush.com.util.Constants.ENDPOINT_FRIEND_LIST
import instau.ayush.com.util.getLongParameter
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

import org.koin.java.KoinJavaComponent.inject

fun Route.friendsListEndpoint() {
    val useCase by inject<FriendListUseCase>(FriendListUseCase::class.java)
    get(ENDPOINT_FRIEND_LIST) {
        val sender = call.getLongParameter("sender", true)
        useCase(sender).collect { response ->
            call.respond(response)
        }
    }
}

fun Route.chatHistoryEndpoint() {
    val useCase by inject<GetHistoryMessagesUseCase>(GetHistoryMessagesUseCase::class.java)
    get(ENDPOINT_CHAT_HISTORY) {
        val receiver = call.getLongParameter("receiver", true)
        val sender = call.getLongParameter("sender", true)
        useCase(sender = sender, receiver = receiver).collect { response ->
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