package optimusfly.plugins

import com.google.gson.Gson
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.launch
import optimusfly.data.db.DatabaseConnection
import optimusfly.data.openai.OpenAI
import optimusfly.data.user.PhoneNumberEntity
import optimusfly.data.user.UserEntity
import optimusfly.data.whatsappApi.WhatsAppApi
import optimusfly.domain.model.dialogflowcx.cxrequest.DialogCXRequestModel
import optimusfly.domain.model.dialogflowcx.cxrequestv3.CxRequestV3Model
import optimusfly.domain.model.gpt.openai.GptResponseModel
import optimusfly.domain.model.gpt.openai.toDialogFlowResponseCXModel
import optimusfly.domain.model.user.*
import optimusfly.domain.model.whatsapp.send_welcome.FacebookLanguage
import optimusfly.domain.model.whatsapp.send_welcome.FacebookTemplate
import optimusfly.domain.model.whatsapp.send_welcome.MessageTemplate
import optimusfly.utils.TokenManager
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt
import java.io.IOException


const val SUCCESS_INSERT = 1
fun Application.userModule() {

    val db = DatabaseConnection.database
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    routing {

        authenticate {
            get("/users") {
                val users = db.from(UserEntity).select().map {
                    val id = it[UserEntity.id]
                    val name = it[UserEntity.name]
                    val lasName = it[UserEntity.lastName]
                    val email = it[UserEntity.email]
                    val password = it[UserEntity.password]
                    val subscription = it[UserEntity.subscription]

                    UserModel(id, name, lasName, email, password, subscription ?: 0, phoneNumber = "")

                }
                call.respond(users)

            }
        }

        get("/get-gpt-response/{text}/") {
            val openai = OpenAI(apiKey = "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")
            val textPrompt = call.request.queryParameters["text"].orEmpty()

            val response = openai.completion(
                prompt = textPrompt, maxTokens = 2048
            )

            if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")

            call.respondText((response.body!!.string()))
        }

        post("/get-gpt-response-from-gpt") {
            val openai = OpenAI(apiKey = "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")

            val request = call.receive<DialogCXRequestModel>()

            val response = openai.completion(
                prompt = request.text.orEmpty(), maxTokens = 2048
            )

            if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")

            val gson = Gson()
            val gptResponse = gson.fromJson(response.body!!.string(), GptResponseModel::class.java)


            call.respond(HttpStatusCode.OK, gptResponse!!.toDialogFlowResponseCXModel())
        }

        post("/get-gpt-response-from-gpt-not-intent") {
            val openai = OpenAI(apiKey = "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")
            val gson2 = Gson()
            val request2 = gson2.fromJson(call.receiveText(), CxRequestV3Model::class.java)


            val response = openai.completion(
                prompt = request2.text.orEmpty(), maxTokens = 2048
            )

            if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")

            val gson = Gson()
            val gptResponse = gson.fromJson(response.body!!.string(), GptResponseModel::class.java)


            call.respond(HttpStatusCode.OK, gptResponse!!.toDialogFlowResponseCXModel())
        }

        get("/get-user-information") {
            call.respondText("Mostrar user information")
        }

        get("/get-user-by-id/{userId}") {
            val userId = call.parameters["userId"]
            call.respondText("The User Id: $userId")
        }

        post("register-user") {
            val request = call.receive<UserRequest>()

            val email = request.email.toString()
            val password = request.hashedPassword()

            val user = db.from(UserEntity).select().where { UserEntity.email eq email }.map { it[UserEntity.email] }
                .firstOrNull()


            if (user != null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    UserResponse(success = true, data = "User already exists, please try with another email")
                )
                return@post
            }

            val result = db.insert(UserEntity) {
                set(it.name, request.name)
                set(it.lastName, request.lastName)
                set(it.email, request.email)
                set(it.password, password)
            }

            if (result == SUCCESS_INSERT) {
                // sen successfully response to the client
                call.respond(
                    HttpStatusCode.OK, UserResponse(success = true, data = "values has been successfully inserted")
                )
            } else {
                call.respond(HttpStatusCode.BadRequest, UserResponse(success = true, data = "Error Inserting"))

            }
        }

        put("update-user-by-id/{userId}") {
            val id = call.parameters["userId"]?.toInt() ?: -1
            val updatedUser = call.receive<UserRequest>()

            val rowAffected = db.update(UserEntity) {
                set(it.name, updatedUser.name)
                where {
                    it.id eq id
                }
            }

            if (rowAffected == 1) {
                call.respond(
                    HttpStatusCode.OK, UserResponse(success = true, data = "values has been successfully updated")
                )
            } else {
                call.respond(HttpStatusCode.BadRequest, UserResponse(success = true, data = "Error updated"))

            }
        }

        delete("/delete-user-by-id/{userId}") {
            val id = call.parameters["userId"]?.toInt() ?: -1
            val rowAffected = db.delete(UserEntity) {
                it.id eq id
            }

            if (rowAffected == 1) {
                call.respond(
                    HttpStatusCode.OK, UserResponse(success = true, data = "values has been successfully deleted")
                )
            } else {
                call.respond(HttpStatusCode.BadRequest, UserResponse(success = true, data = "Error deleted"))
            }
        }

        post("/login") {
            val userCredential = call.receive<UserCredentials>()

            val email = userCredential.email.toString()
            val password = userCredential.password

            val user = db.from(UserEntity).select().where {
                UserEntity.email eq email
            }.map {
                val id = it[UserEntity.id]
                val email = it[UserEntity.email]!!
                val password = it[UserEntity.password]!!
                optimusfly.bff.model.UserModel(id, email, password)
            }.firstOrNull()

            if (user == null) {
                call.respond(
                    HttpStatusCode.BadRequest, UserResponse(success = false, data = "Invalid email or password")
                )
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password, user.password)

            if (!doesPasswordMatch) {
                call.respond(
                    HttpStatusCode.Unauthorized, UserResponse(success = false, data = "Invalid email or password")
                )
            }

            val token = tokenManager.generateJWTToken(user)
            call.respond(HttpStatusCode.OK, UserResponse(success = true, data = token))
        }

        authenticate {
            patch("update-subscription") {
                val request = call.receive<UpdateSubscriptionRequest>()
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asInt()
                val newSubscription = request.newSubscription

                val user = db.from(UserEntity).select().where { UserEntity.id eq userId }.map { it[UserEntity.id] }
                    .firstOrNull()

                if (user == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        UserResponse(success = false, data = "User not found, please check the user ID")
                    )
                    return@patch
                }

                val result = db.update(UserEntity) {
                    where { it.id eq userId }
                    set(it.subscription, newSubscription)
                }

                if (result > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        UserResponse(success = true, data = "Subscription has been successfully updated")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest, UserResponse(success = false, data = "Error updating subscription")
                    )
                    return@patch
                }
            }

            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal!!.payload.getClaim("email").asString()
                val user =
                    db.from(UserEntity).leftJoin(PhoneNumberEntity, on = UserEntity.id eq PhoneNumberEntity.userId)
                        .select().where { UserEntity.email eq email }.map {
                            UserModel(
                                it[UserEntity.id],
                                it[UserEntity.name],
                                it[UserEntity.lastName],
                                it[UserEntity.email],
                                it[UserEntity.password],
                                it[UserEntity.subscription],
                                it[PhoneNumberEntity.phoneNumber]
                            )
                        }.firstOrNull()

                user?.let {
                    call.respond(it)
                } ?: call.respond(
                    HttpStatusCode.InternalServerError, UserResponse(success = true, data = "Error Inserting")
                )
            }

            post("/update-phone") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal!!.payload.getClaim("email").asString()
                val user =
                    db.from(UserEntity).leftJoin(PhoneNumberEntity, on = UserEntity.id eq PhoneNumberEntity.userId)
                        .select().where { UserEntity.email eq email }.map {
                            UserModel(
                                it[UserEntity.id],
                                it[UserEntity.name],
                                it[UserEntity.lastName],
                                it[UserEntity.email],
                                it[UserEntity.password],
                                it[UserEntity.subscription],
                                it[PhoneNumberEntity.phoneNumber]
                            )
                        }.firstOrNull()

                user?.let {
                    call.respond(it)
                } ?: call.respond(
                    HttpStatusCode.InternalServerError, UserResponse(success = true, data = "Error Inserting")
                )
            }


            patch("update-phone-number") {
                val request = call.receive<UpdatePhoneNumberRequest>()
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asInt()
                val newPhoneNumber = request.newPhoneNumber

                val phoneNumberValue =
                    db.from(PhoneNumberEntity).select().where { PhoneNumberEntity.phoneNumber eq newPhoneNumber }
                        .map { it[PhoneNumberEntity.phoneNumber] }.firstOrNull()

                if (phoneNumberValue != null) {
                    call.respond(
                        HttpStatusCode.BadRequest, UserResponse(
                            success = false, data = "New phone number already exists, please try with another phone"
                        )
                    )
                    return@patch
                }


                if (newPhoneNumber.isNotBlank()) {
                    val result = db.update(PhoneNumberEntity) {
                        where { it.userId eq userId }
                        set(it.phoneNumber, newPhoneNumber)
                        set(it.userId, userId)
                    }

                    if (result > 0) {
                        call.respond(
                            HttpStatusCode.OK,
                            UserResponse(success = true, data = "Phone number has been successfully updated")
                        )
                        sendWhatsappMessage(newPhoneNumber, this)
                    } else {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            UserResponse(success = false, data = "Error updating phone number")
                        )
                        return@patch
                    }
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest, UserResponse(success = false, data = "Error updating phone number")
                    )
                    return@patch
                }
            }

            post("register-phone-number") {
                val request = call.receive<PhoneNumberRequest>()
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asInt()
                val phoneNumber = request.phoneNumber
                val phoneNumberFormatted = formatPhoneNumber(phoneNumber)

                val userExists =
                    db.from(UserEntity).select(UserEntity.id).where { UserEntity.id eq userId }.totalRecords > 0
                if (!userExists) {
                    call.respond(
                        HttpStatusCode.BadRequest, UserResponse(success = false, data = "User not found")
                    )
                    return@post
                }

                val phoneNumberRecord =
                    db.from(PhoneNumberEntity).select().where { PhoneNumberEntity.phoneNumber eq phoneNumberFormatted }
                        .map { Pair(it[PhoneNumberEntity.phoneNumber], it[PhoneNumberEntity.userId]) }.firstOrNull()

                if (phoneNumberRecord != null) {
                    val associatedUserId = phoneNumberRecord.second
                    if (associatedUserId == userId) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            UserResponse(success = false, data = "PhoneNumber is already associated with your account")
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.BadRequest, UserResponse(
                                success = false, data = "PhoneNumber already exists, please try with another Phone"
                            )
                        )
                    }
                    return@post
                }

                val existingUserPhoneNumber =
                    db.from(PhoneNumberEntity).select().where { PhoneNumberEntity.userId eq userId }
                        .map { it[PhoneNumberEntity.phoneNumber] }.firstOrNull()

                if (existingUserPhoneNumber != null && existingUserPhoneNumber != "") {
                    call.respond(
                        HttpStatusCode.BadRequest, UserResponse(
                            success = false,
                            data = "User already has a registered phone number, cannot insert another one"
                        )
                    )
                    return@post
                }

                val result = db.insert(PhoneNumberEntity) {
                    set(it.phoneNumber, phoneNumberFormatted)
                    set(it.userId, userId)
                }

                if (result == SUCCESS_INSERT) {
                    call.respond(
                        HttpStatusCode.OK, UserResponse(success = true, data = "values has been successfully inserted")
                    )
                    sendWhatsappMessage(phoneNumberFormatted, this)
                } else {
                    call.respond(HttpStatusCode.BadRequest, UserResponse(success = false, data = "Error Inserting"))
                }
            }

        }
    }
}

fun formatPhoneNumber(phoneNumber: String): String {
    return phoneNumber.replace("-", "")
}


fun sendWhatsappMessage(phoneNumber: String, launch: PipelineContext<Unit, ApplicationCall>) {

    val whatsAppApi = WhatsAppApi()

    val messageRequest = MessageTemplate(
        messaging_product = "whatsapp", to = phoneNumber, type = "template", template = FacebookTemplate(
            name = "autorization", language = FacebookLanguage(
                code = "es"
            )
        )
    )

    launch.launch {
        val response = whatsAppApi.sendMessageTemplate(
            messageRequest
        )
        if (!response.isSuccessful) throw IOException("Unexpected code ${response.message}  y ${response.code}")

    }
}

