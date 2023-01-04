package optimusfly.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userModule() {
    routing {
        get("/get-user-information") {
            call.respondText("Mostrar user information")
        }
    }
}