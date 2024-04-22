package instaU.ayush.com

import instaU.ayush.com.dao.DatabaseFactory
import instaU.ayush.com.di.configureDI
import instaU.ayush.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val jwtIssuer = System.getenv("jwt.domain")
fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
    fun ayush() {
        println(jwtIssuer)
    }
    ayush()
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureDI()
    configureSecurity()
    configureRouting()
}
