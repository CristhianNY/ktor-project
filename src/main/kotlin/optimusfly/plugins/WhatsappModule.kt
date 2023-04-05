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
import optimusfly.data.user.PhoneNumberEntity
import optimusfly.data.user.UserEntity
import optimusfly.data.whatsapp.WhatsappMessageEntity
import optimusfly.data.whatsappApi.WhatsAppApi
import optimusfly.domain.model.gpt.openai.chatGpt.ChatGptResponseModel
import optimusfly.domain.model.user.UserModel
import optimusfly.domain.model.whatsapp.WebHookPetitionModel
import optimusfly.domain.model.whatsapp.send.*
import optimusfly.domain.model.whatsapp.send_welcome.FacebookLanguage
import optimusfly.domain.model.whatsapp.send_welcome.FacebookTemplate
import optimusfly.domain.model.whatsapp.send_welcome.MessageTemplate
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
            val phoneNumber = request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.from.orEmpty()
            val user = db.from(UserEntity).leftJoin(PhoneNumberEntity, on = UserEntity.id eq PhoneNumberEntity.userId)
                .select().where { PhoneNumberEntity.phoneNumber eq phoneNumber }.map {
                    UserModel(
                        it[UserEntity.id],
                        it[UserEntity.name],
                        it[UserEntity.lastName],
                        it[UserEntity.email],
                        it[UserEntity.password],
                        it[UserEntity.subscription],
                        it[PhoneNumberEntity.phoneNumber],
                        it[UserEntity.credits]

                    )
                }.firstOrNull()

            val whatsAppApi = WhatsAppApi()

            if (user?.subscription == 1 || user?.credit ?: 0 > 0) {
                var gptAnswer: ChatGptResponseModel? = null

                val messageId = request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.id

                val messagePrompt =
                    request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.text?.body.orEmpty()

                db.insert(WhatsappMessageEntity) {
                    set(it.idMessage, messageId)
                }

                val message: Int = db.from(WhatsappMessageEntity).select()
                    .where { WhatsappMessageEntity.idMessage eq messageId.orEmpty() }
                    .map { it[WhatsappMessageEntity.idMessage] }
                    .size

                println("mensaje id" + message + "con" + messageId)


                if (request2.entry?.first()?.changes?.first()?.value?.statuses?.first() == null) {
                    launch(Dispatchers.IO) {
                        val gptResponse = async {
                            val openai = OpenAI(apiKey = "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")

                            /** val response: Response = openai.completion(
                            prompt = messagePrompt,
                            maxTokens = 2048
                            )

                            val gson = Gson()
                            gptAnswer = gson.fromJson(response.body!!.string(), GptResponseModel::class.java)**/

                            val messages = listOf(
                                mapOf("role" to "user", "content" to "$messagePrompt")
                            )

                            val response: Response = openai.chatCompletion("gpt-3.5-turbo", messages)

                            val gson = Gson()
                            gptAnswer = gson.fromJson(response.body!!.string(), ChatGptResponseModel::class.java)

                        }

                        gptResponse.await()


                        val gptMessageResponse = MessageToSendModel(
                            messaging_product = "whatsapp",
                            to = request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.from.orEmpty(),
                            type = "text",
                            text = Text(
                                preview_url = false,
                                body = gptAnswer?.choices?.first()?.message?.content.orEmpty()
                            )
                        )

                        val response = whatsAppApi.sendMessage(
                            gptMessageResponse
                        )

                        if (response.isSuccessful) {

                            if (user?.credit != 0) {
                                db.update(UserEntity) {
                                    where { it.id eq user?.id!! }
                                    set(it.credits, user?.credit ?: 0 - 1)
                                }
                            }
                        }
                        if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")
                    }
                }
            } else {
                val notCreditMessageTemplate = MessageTemplate(
                    messaging_product = "whatsapp",
                    to = request2.entry?.first()?.changes?.first()?.value?.messages?.first()?.from.orEmpty(),
                    type = "template",
                    template = FacebookTemplate(name = "not_credit", language = FacebookLanguage("es_ES"))
                )

                val response = whatsAppApi.sendMessageTemplate(
                    notCreditMessageTemplate
                )
                if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")
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

            val whatsAppApi = WhatsAppApi()

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