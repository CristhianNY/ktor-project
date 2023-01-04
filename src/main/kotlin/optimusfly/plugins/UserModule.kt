package optimusfly.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import optimusfly.domain.model.UserModel

fun Application.userModule() {
    routing {
        get("/get-user-information") {
            call.respondText("Mostrar user information")
        }

        get("/get-user-by-id/{userId}") {
            val userId = call.parameters["userId"]
            call.respondText("The User Id: $userId")
        }

        post("/login") {
            val user = call.receive<UserModel>()


        }
    }
}

