package optimusfly.plugins

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import optimusfly.data.whatsappApi.WhatsAppApi
import optimusfly.domain.model.whatsapp.WebHookPetitionModel
import optimusfly.domain.model.whatsapp.get.ResponseMessageSuccess
import optimusfly.domain.model.whatsapp.send.Language
import optimusfly.domain.model.whatsapp.send.MessageModel
import optimusfly.domain.model.whatsapp.send.MessageResponseModel
import optimusfly.domain.model.whatsapp.send.Template
import java.io.IOException


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

            println("este es:"+request2)
            val mockMessage = MessageModel(
                messaging_product = request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.text?.body.orEmpty(),
                to = "573157119388",
                type = "template",
                template = Template(
                    name = "hello_world",
                    language = Language(
                        code = "en_US"
                    )
                )
            )

            val whatsAppApi =
                WhatsAppApi("EAAMZBu7GdAScBAGS5fhE9BlxOZCUE71leopZCHXrlPZAURQZBpC4Lg2wRCfv8ipA9PLlusfTyVYjdgzqdrBHY4zO5CxZBqZADMg6Go90evMPNTkdYx0OCz1vs5XqTKxl7ZCrQwrfpdECoIw63k261jFieS0xci8reVtMEv8VoSoJYJpXvJ91Lk0yGQdlP7kEJMJ614voSMBF9varIYdKc6ZBa")

          /**  launch(Dispatchers.IO) {
            val response = whatsAppApi.sendMessage(
            mockMessage
            )
            if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")
            }**/

            call.respond(HttpStatusCode.OK)
        }


    }
}