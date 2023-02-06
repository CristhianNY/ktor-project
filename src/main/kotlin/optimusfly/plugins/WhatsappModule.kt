package optimusfly.plugins

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Response
import optimusfly.data.db.DatabaseConnection
import optimusfly.data.openai.OpenAI
import optimusfly.data.whatsapp.WhatsappMessageEntity
import optimusfly.data.whatsappApi.WhatsAppApi
import optimusfly.domain.model.gpt.openai.GptResponseModel
import optimusfly.domain.model.whatsapp.WebHookPetitionModel
import optimusfly.domain.model.whatsapp.send.*
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

            var gptAnswer: GptResponseModel? = null

            val messageId = request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.id

            val messagePrompt = request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.text?.body.orEmpty()

            db.insert(WhatsappMessageEntity) {
                set(it.idMessage, messageId)
            }

            val message: Int = db.from(WhatsappMessageEntity).select()
                .where { WhatsappMessageEntity.idMessage eq messageId.orEmpty() }
                .map { it[WhatsappMessageEntity.idMessage] }
                .size

            println("mensaje id" + message + "con" + messageId)


            if (request2.entry?.first()?.changes?.first()?.value?.statuses?.first() == null) {
                val whatsAppApi =
                    WhatsAppApi("EAAMZBu7GdAScBAKGOkhaVZA9FueJWQupu72vL4GMSrEZA4NoRerYGaecbAMOUpzDaDreTyRShZCwNS26UHJC8ExxnKgdEZASZBk9xGOmPRm38WJtuMxwPtd5zognI1ls8kBVtN1KmgnAMGNdkcyXM0nEyfllZBRRp3H7KodPb7YZBzlhjOb2bZABMY14OV6XqUVrjGE0xFOxLFK4YQamIYu3k")



                launch(Dispatchers.IO) {
                    val gptResponse =   async {
                        val openai = OpenAI(apiKey = "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")

                        val response: Response = openai.completion(
                            prompt = messagePrompt,
                            maxTokens = 2048
                        )

                        val gson = Gson()
                        gptAnswer = gson.fromJson(response.body!!.string(), GptResponseModel::class.java)

                    }

                    gptResponse.await()


                    val mockTextMessage = MessageToSendModel(
                        messaging_product = "whatsapp",
                        to = "573157119388",
                        type = "text",
                        text = Text(
                            preview_url = false,
                            body = gptAnswer?.choices?.first()?.text.orEmpty()
                        )
                    )
                    val response = whatsAppApi.sendMessage(
                        mockTextMessage
                    )
                    if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")
                }
            }

            call.respond(HttpStatusCode.OK)

        }

        post("/send-whatsapp") {
            val gson2 = Gson()
            val request2 = gson2.fromJson(call.receiveText(), WebHookPetitionModel::class.java)

            val mockTextMessage = MessageToSendModel(
                messaging_product = "whatsapp",
                to = "573157119388",
                type = "text",
                text = Text(
                    preview_url = false,
                    body = "MESSAGE_CONTENT"
                )
            )

            val whatsAppApi =
                WhatsAppApi("EAAMZBu7GdAScBAKGOkhaVZA9FueJWQupu72vL4GMSrEZA4NoRerYGaecbAMOUpzDaDreTyRShZCwNS26UHJC8ExxnKgdEZASZBk9xGOmPRm38WJtuMxwPtd5zognI1ls8kBVtN1KmgnAMGNdkcyXM0nEyfllZBRRp3H7KodPb7YZBzlhjOb2bZABMY14OV6XqUVrjGE0xFOxLFK4YQamIYu3k")

            launch(Dispatchers.IO) {
                val response = whatsAppApi.sendMessage(
                    mockTextMessage
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