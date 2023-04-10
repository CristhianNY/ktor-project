package optimusfly.plugins

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
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
import optimusfly.domain.model.map_errors.ErrorMapper
import optimusfly.domain.model.map_errors.ErrorMapper.GENERIC_ERROR
import optimusfly.domain.model.map_errors.ErrorMapper.PHONE_NUMBER_REQUIRED
import optimusfly.domain.model.map_errors.ErrorMapper.USER_NOT_FOUND
import optimusfly.domain.model.user.*
import optimusfly.domain.model.whatsapp.send_welcome.FacebookLanguage
import optimusfly.domain.model.whatsapp.send_welcome.FacebookTemplate
import optimusfly.domain.model.whatsapp.send_welcome.MessageTemplate
import optimusfly.utils.TokenManager
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
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
                    val credit = it[UserEntity.credits]

                    UserModel(id, name, lasName, email, password, subscription ?: 0, phoneNumber = "", credit)

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
            val request: UserRequest = call.receive<UserRequest>()

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
                val idToken = it[UserEntity.idToken]!!
                val credits = it[UserEntity.credits]!!
                optimusfly.bff.model.UserModel(id, email, password, idToken, credits)
            }.firstOrNull()

            if (user == null) {
                val error = ErrorMapper.INVALID_EMAIL_OR_PASSWORD
                call.respond(HttpStatusCode.BadRequest, UserResponse(success = false, data = error))
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password, user.password)

            if (!doesPasswordMatch) {
                val error = ErrorMapper.INVALID_EMAIL_OR_PASSWORD
                call.respond(HttpStatusCode.Unauthorized, UserResponse(success = false, data = error))
                return@post
            }

            val token = tokenManager.generateJWTToken(user)
            call.respond(HttpStatusCode.OK, UserResponse(success = true, data = token))
        }

        authenticate {

            patch("add-credits") {
                val request = call.receive<AddCreditRequest>()
                val principal = call.principal<JWTPrincipal>()
                val credit = request.credit
                val email = principal!!.payload.getClaim("email").asString()

                val user = db.from(UserEntity).select().where {
                    UserEntity.email eq email
                }.map {
                    val id = it[UserEntity.id]
                    val email = it[UserEntity.email]!!
                    val idToken = it[UserEntity.idToken]!!
                    val password = it[UserEntity.password]
                    val credits = it[UserEntity.credits]
                    optimusfly.bff.model.UserModel(id, email, password, idToken, credits)
                }.firstOrNull()

                if (user == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        UserResponse(success = false, data = USER_NOT_FOUND)
                    )
                    return@patch
                }

                val result = db.update(UserEntity) {
                    where { it.email eq email }
                    set(it.credits, user.credits?.plus(credit))
                }

                val userUpdated = db.from(UserEntity).select().where {
                    UserEntity.email eq email
                }.map {
                    val id = it[UserEntity.id]
                    val email = it[UserEntity.email]!!
                    val idToken = it[UserEntity.idToken]!!
                    val password = it[UserEntity.password]
                    val credits = it[UserEntity.credits]
                    optimusfly.bff.model.UserModel(id, email, password, idToken, credits)
                }.firstOrNull()
                if (result > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        UserResponse(success = true, data = userUpdated?.credits)
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest, UserResponse(success = false, data = "Error updating subscription")
                    )
                    return@patch
                }
            }

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
                        UserResponse(success = false, data = USER_NOT_FOUND)
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
                                it[PhoneNumberEntity.phoneNumber],
                                it[UserEntity.credits]

                            )
                        }.firstOrNull()

                user?.let {
                    call.respond(it)
                } ?: call.respond(
                    HttpStatusCode.InternalServerError, UserResponse(success = true, data = "Error Getting Data")
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
                                it[PhoneNumberEntity.phoneNumber],
                                it[UserEntity.credits]
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

                val userHasPhoneNumber =
                    db.from(PhoneNumberEntity).select().where { PhoneNumberEntity.userId eq userId }
                        .map { it[PhoneNumberEntity.phoneNumber] }.firstOrNull()

                if (phoneNumberValue != null) {
                    call.respond(
                        HttpStatusCode.BadRequest, UserResponse(
                            success = false, data = ErrorMapper.NEW_PHONE_NUMBER_ALREADY_REGISTERED_WITH_OTHER_USER
                        )
                    )
                    return@patch
                }

                if (newPhoneNumber.isNotBlank()) {
                    val result = if (userHasPhoneNumber != null) {
                        db.update(PhoneNumberEntity) {
                            where { it.userId eq userId }
                            set(it.phoneNumber, newPhoneNumber)
                            set(it.userId, userId)
                        }
                    } else {
                        db.insert(PhoneNumberEntity) {
                            set(it.phoneNumber, newPhoneNumber)
                            set(it.userId, userId)
                        }
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
                            UserResponse(
                                success = false,
                                data = GENERIC_ERROR,
                            )
                        )
                        return@patch
                    }
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest, UserResponse(success = false, data = PHONE_NUMBER_REQUIRED)
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
                    val error = ErrorMapper.USER_NOT_FOUND
                    call.respond(HttpStatusCode.BadRequest, UserResponse(success = false, data = error))
                    return@post
                }

                val phoneNumberRecord = db.from(PhoneNumberEntity).select()
                    .where { PhoneNumberEntity.phoneNumber eq phoneNumberFormatted }
                    .map { Pair(it[PhoneNumberEntity.phoneNumber], it[PhoneNumberEntity.userId]) }.firstOrNull()

                if (phoneNumberRecord != null) {
                    val associatedUserId = phoneNumberRecord.second
                    if (associatedUserId == userId) {
                        val error = ErrorMapper.PHONE_ALREADY_ASSOCIATED
                        call.respond(HttpStatusCode.BadRequest, UserResponse(success = false, data = error))
                    } else {
                        val error = ErrorMapper.PHONE_ALREADY_EXISTS
                        call.respond(HttpStatusCode.BadRequest, UserResponse(success = false, data = error))
                    }
                    return@post
                }

                val existingUserPhoneNumber =
                    db.from(PhoneNumberEntity).select().where { PhoneNumberEntity.userId eq userId }
                        .map { it[PhoneNumberEntity.phoneNumber] }.firstOrNull()

                if (existingUserPhoneNumber != null && existingUserPhoneNumber != "") {
                    val error = ErrorMapper.PHONE_ALREADY_REGISTERED
                    call.respond(HttpStatusCode.BadRequest, UserResponse(success = false, data = error))
                    return@post
                }

                val result = db.insert(PhoneNumberEntity) {
                    set(it.phoneNumber, phoneNumberFormatted)
                    set(it.userId, userId)
                }

                if (result == SUCCESS_INSERT) {
                    val message = "values has been successfully inserted"
                    call.respond(HttpStatusCode.OK, UserResponse(success = true, data = message))
                    sendWhatsappMessage(phoneNumberFormatted, this)
                } else {
                    val error = ErrorMapper.ERROR_INSERTING
                    call.respond(HttpStatusCode.BadRequest, UserResponse(success = false, data = error))
                }
            }
        }
        post("/login-with-id-token") {
            val userCredential = call.receive<UserCredentialsWithToken>()

            var userFound: optimusfly.bff.model.UserModel? = null
            val email = userCredential.email.toString()
            val idToken = userCredential.hashedIdToken()

            userFound = db.from(UserEntity).select().where {
                UserEntity.email eq email
            }.map {
                val id = it[UserEntity.id]
                val email = it[UserEntity.email]!!
                val idToken = it[UserEntity.idToken]!!
                val password = it[UserEntity.password]
                val credits = it[UserEntity.credits]
                optimusfly.bff.model.UserModel(id, email, password, idToken, credits)
            }.firstOrNull()

            if (userFound == null) {
                val result = db.insert(UserEntity) {
                    set(it.name, userCredential.name)
                    set(it.lastName, userCredential.lastName)
                    set(it.email, userCredential.email)
                    set(it.password, "")
                    set(it.idToken, userCredential.hashedIdToken())
                }

                if (result == SUCCESS_INSERT) {

                    userFound = db.from(UserEntity).select().where {
                        UserEntity.email eq email
                    }.map {
                        val id = it[UserEntity.id]
                        val email = it[UserEntity.email]!!
                        val password = it[UserEntity.password]!!
                        val idToken = it[UserEntity.idToken]!!
                        val credits = it[UserEntity.credits]!!
                        optimusfly.bff.model.UserModel(id, email, password, idToken, credits)
                    }.firstOrNull()
                    userFound?.let {
                        val token = tokenManager.generateJWTToken(it)
                        call.respond(HttpStatusCode.OK, UserResponse(success = true, data = token))
                    }?.run {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            UserResponse(success = true, data = "Error Creating Token")
                        )
                    }

                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        UserResponse(success = true, data = "Error Inserting Registering")
                    )
                }
                return@post
            }

            val payload = verifyGoogleIdToken(userCredential.idToken.orEmpty())

            if (payload != null) {
                val token = tokenManager.generateJWTToken(userFound)
                call.respond(HttpStatusCode.OK, UserResponse(success = true, data = token))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            }
        }
    }

}

suspend fun verifyGoogleIdToken(idTokenString: String): GoogleIdToken.Payload? {
    val logger = LoggerFactory.getLogger("verifyGoogleIdToken")
    val transport = NetHttpTransport()
    val jsonFactory: JsonFactory = JacksonFactory()
    logger.info("Tel id del token es $idTokenString")
    val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(listOf("350290328138-l97egloqh572lvlfdta27pp9529i8sku.apps.googleusercontent.com")) // Reemplaza "YOUR_CLIENT_ID" con el ID de cliente de Google de tu proyecto
        .build()

    logger.info("este es el verifier $verifier")

    return try {
        val idToken: GoogleIdToken = verifier.verify("eyJhbGciOiJSUzI1NiIsImtpZCI6Ijk2OTcxODA4Nzk2ODI5YTk3MmU3OWE5ZDFhOWZmZjExY2Q2MWIxZTMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIzNTAyOTAzMjgxMzgtbDk3ZWdsb3FoNTcybHZsZmR0YTI3cHA5NTI5aThza3UuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIzNTAyOTAzMjgxMzgtcmhjYThhMTZva2VmZW01Ymd2bGhobW44MjJmdmE0OWYuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDU2NjMxMzQwNzYyODIwNDA2OTQiLCJlbWFpbCI6ImNyaXN0aGlhbjQ1NDVAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJDUklTVEhJQU4gQk9OSUxMQSIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BR05teXhhNnQtTjRBcVZvSktSVlpBWVV2R2FaQlpvcEVwSzNzeU45dmJkbFhnPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IkNSSVNUSElBTiIsImZhbWlseV9uYW1lIjoiQk9OSUxMQSIsImxvY2FsZSI6ImVuIiwiaWF0IjoxNjgxMTYzNjQwLCJleHAiOjE2ODExNjcyNDB9.e0etieWMVl1h5LaC3jO63kBmuQge2Aj1k89-A-ByZe6VDy-Nb29Bnb2iD_ka5xYJ8S31GrCCL8IPAdBW-jHWeb5UQIwa0mxLtraNi5LsXiF6Hu46ydHyQtMF0ussGogaywDSdQGVbV25plXbH5Y0fPWG18OZTQ-RLmFxGjctmCZ31Q4Ev9QW6rk4zdu1hsDrEwkwMXM3js30qaYmR06omUyxz-VzS5BLZNyJfc3AKdBv2QLdNsBv_Vcf4uTrpG2zAElUXz_jXJL2r3Wp5R8nXjDzti4TphFN-PypCFXFkmRNcRCO5eb5PfWJz_pIxGx40-i7xs0yq2tL-NGrUWesww")
        if (idToken != null) {
            logger.info("Token verificado correctamente")
            idToken.payload
        } else {
            logger.warn("Token no verificado")
            null
        }
    } catch (e: Exception) {
        logger.error("Error al verificar el token: ${e.message}", e)
        null
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

