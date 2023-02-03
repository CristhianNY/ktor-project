package optimusfly.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.whatsappModule() {

    routing {
        get("/webhooks") {
            val token = "chatCristhianBonillaGpt"
            val hubMode = call.parameters["hub.mode"] ?: throw IllegalStateException("hub.mode parameter is missing")
            val hubChallenge = call.parameters["hub.challenge"] ?: throw IllegalStateException("hub.challenge parameter is missing")
            val hubVerifyToken = call.parameters["hub.verify_token"] ?: throw IllegalStateException("hub.verify_token parameter is missing")

            if (hubMode != "subscribe") throw IllegalStateException("Invalid hub.mode")

            if (hubVerifyToken != token) throw IllegalStateException("Invalid hub.verify_token")

            call.respondText(hubChallenge)
        }

    }
}