package instau.ayush.com

import instau.ayush.com.dao.DatabaseFactory
import instau.ayush.com.di.configureDI
import instau.ayush.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureDI()
    configureSecurity()
    configureRouting()
    configureSockets()
    configureSession()
    this.configureCORS()
}
