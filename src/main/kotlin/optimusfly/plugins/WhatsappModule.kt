package optimusfly.plugins

import com.google.gson.Gson
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import optimusfly.domain.model.dialogflowcx.cxrequestv3.CxRequestV3Model
import optimusfly.domain.model.whatsapp.WebHookPetitionModel


fun Application.whatsappModule() {

    routing {
        get("/webhooks") {
            val token = "chatCristhianBonillaGpt"
            val hubMode = call.parameters["hub.mode"] ?: throw IllegalStateException("hub.mode parameter is missing")
            val hubChallenge =
                call.parameters["hub.challenge"] ?: throw IllegalStateException("hub.challenge parameter is missing")
            val hubVerifyToken = call.parameters["hub.verify_token"]
                ?: throw IllegalStateException("hub.verify_token parameter is missing")

            if (hubMode != "subscribe") throw IllegalStateException("Invalid hub.mode")

            if (hubVerifyToken != token) throw IllegalStateException("Invalid hub.verify_token")

            call.respondText(hubChallenge)
        }

        post("/webhooks") {
            val gson2 = Gson()
            val request2 = gson2.fromJson(call.receiveText(), WebHookPetitionModel::class.java)

            call.respondText(request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.text?.body.orEmpty())
        }

    }
}