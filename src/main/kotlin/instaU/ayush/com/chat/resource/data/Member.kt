package instaU.ayush.com.chat.resource.data

import io.ktor.websocket.*
data class Member(
    val sender: String,
    val sessionId: String,
    val webSocket: WebSocketSession
)