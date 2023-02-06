package optimusfly.plugins

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import optimusfly.data.db.DatabaseConnection
import optimusfly.data.whatsapp.WhatsappMessageEntity
import optimusfly.data.whatsappApi.WhatsAppApi
import optimusfly.domain.model.user.UserResponse
import optimusfly.domain.model.whatsapp.WebHookPetitionModel
import optimusfly.domain.model.whatsapp.send.Language
import optimusfly.domain.model.whatsapp.send.MessageModel
import optimusfly.domain.model.whatsapp.send.Template
import org.ktorm.dsl.*
import java.io.IOException


fun Application.whatsappModule() {
    val db = DatabaseConnection.database
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

            val messageId = request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.id

            println(messageId)

            val message = db.from(WhatsappMessageEntity).select()
                .where { WhatsappMessageEntity.idMessage eq messageId.orEmpty() }
                .map { it[WhatsappMessageEntity.idMessage] }
                .firstOrNull()

            println(message)

            if (message == null) {
                val whatsAppApi =
                    WhatsAppApi("2EAAMZBu7GdAScBAKGOkhaVZA9FueJWQupu72vL4GMSrEZA4NoRerYGaecbAMOUpzDaDreTyRShZCwNS26UHJC8ExxnKgdEZASZBk9xGOmPRm38WJtuMxwPtd5zognI1ls8kBVtN1KmgnAMGNdkcyXM0nEyfllZBRRp3H7KodPb7YZBzlhjOb2bZABMY14OV6XqUVrjGE0xFOxLFK4YQamIYu3k")

                launch(Dispatchers.IO) {
                    val response = whatsAppApi.sendMessage(
                        mockMessage
                    )
                    if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")
                }
            }


            if (messageId != null) {
                db.insert(WhatsappMessageEntity) {
                    set(it.idMessage, messageId)
                }
            }

            call.respond(HttpStatusCode.OK)

        }

        post("/send-whatsapp") {
            val gson2 = Gson()
            val request2 = gson2.fromJson(call.receiveText(), WebHookPetitionModel::class.java)

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
                WhatsAppApi("EAAMZBu7GdAScBAKGOkhaVZA9FueJWQupu72vL4GMSrEZA4NoRerYGaecbAMOUpzDaDreTyRShZCwNS26UHJC8ExxnKgdEZASZBk9xGOmPRm38WJtuMxwPtd5zognI1ls8kBVtN1KmgnAMGNdkcyXM0nEyfllZBRRp3H7KodPb7YZBzlhjOb2bZABMY14OV6XqUVrjGE0xFOxLFK4YQamIYu3k")

            launch(Dispatchers.IO) {
                val response = whatsAppApi.sendMessage(
                    mockMessage
                )
                if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")
            }

            call.respond(HttpStatusCode.OK)
        }

        get("/get-messages-id") {
            val message = db.from(WhatsappMessageEntity).select()
                .map { it[WhatsappMessageEntity.idMessage] }
            call.respond(HttpStatusCode.OK, message)
        }

    }
}