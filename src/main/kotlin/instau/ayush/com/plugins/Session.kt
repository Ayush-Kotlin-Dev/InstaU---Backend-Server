package instau.ayush.com.plugins

import instau.ayush.com.chat.data.dao.ChatSessionEntity
import instau.ayush.com.chat.domain.repository.ChatRepository
import instau.ayush.com.util.IdGenerator
import instau.ayush.com.util.getLongParameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.koin.java.KoinJavaComponent.inject

import io.ktor.util.pipeline.*
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class RateLimiter(private val requestsPerMinute: Int) {
    private val requestCounts = ConcurrentHashMap<String, MutableList<Instant>>()

    fun isAllowed(clientId: String): Boolean {
        val now = Instant.now()
        val windowStart = now.minus(Duration.ofMinutes(1))

        val requestTimestamps = requestCounts.computeIfAbsent(clientId) { mutableListOf() }
        requestTimestamps.removeIf { it.isBefore(windowStart) }

        return if (requestTimestamps.size < requestsPerMinute) {
            requestTimestamps.add(now)
            true
        } else {
            false
        }
    }
}


fun Application.configureSession() {

    install(Sessions) {
        cookie<ChatSessionEntity>("MY_SESSION")
    }

    intercept(ApplicationCallPipeline.Plugins) {

        val chatRepository by inject<ChatRepository>(ChatRepository::class.java)

        // Check if the request path is chat/connect'
        if (call.request.path() == "/chat/connect") {
            if (call.sessions.get<ChatSessionEntity>() == null) {
                val sender = call.getLongParameter("sender", true)
                val receiver = call.getLongParameter("receiver", true)

                var sessionId = chatRepository.checkSessionAvailability(sender, receiver)

                if (sessionId == null)
                    sessionId = chatRepository.createNewSession(sender, receiver)

                call.sessions.set(
                    ChatSessionEntity(
                        sender = sender, receiver = receiver, sessionId = sessionId , id = IdGenerator.generateId()
                    )
                )
            }
        }
    }
}

fun Application.configureRateLimitedApiKeyAuthentication() {
    val validApiKeys = setOf("your-valid-api-key")
    val rateLimiter = RateLimiter(requestsPerMinute = 60)

    intercept(ApplicationCallPipeline.Call) {
        // API Key Authentication
        val apiKey = call.request.headers["Api-Key"]
        if (apiKey == null || apiKey !in validApiKeys) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid API Key")
            finish()
            return@intercept
        }

        // Rate Limiting
        //TODO        // can use API key as a client identifier if needed
        //        // to rate limit based on API key instead of client IP
        val clientId = call.request.origin.remoteHost // Using client's IP as an identifier
        if (!rateLimiter.isAllowed(clientId)) {
            call.respond(HttpStatusCode.TooManyRequests, "Rate limit exceeded")
            finish()
        }
    }
}
