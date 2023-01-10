package optimusfly.plugins

import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import optimusfly.data.db.DatabaseConnection
import optimusfly.data.user.UserEntity
import optimusfly.domain.model.user.UserCredentials
import optimusfly.domain.model.user.UserModel
import optimusfly.domain.model.user.UserRequest
import optimusfly.domain.model.user.UserResponse
import optimusfly.utils.TokenManager
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

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

                    UserModel(id, name, lasName, email, password)

                }
                call.respond(users)

            }
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

            val user = db.from(UserEntity).select()
                .where { UserEntity.email eq email }
                .map { it[UserEntity.email] }
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
                    HttpStatusCode.OK,
                    UserResponse(success = true, data = "values has been successfully inserted")
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
                    HttpStatusCode.OK,
                    UserResponse(success = true, data = "values has been successfully updated")
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
                    HttpStatusCode.OK,
                    UserResponse(success = true, data = "values has been successfully deleted")
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
                    HttpStatusCode.BadRequest,
                    UserResponse(success = false, data = "Invalid email or password")
                )
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password, user.password)

            if (!doesPasswordMatch) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    UserResponse(success = false, data = "Invalid email or password")
                )
            }

            val token = tokenManager.generateJWTToken(user)
            call.respond(HttpStatusCode.OK, UserResponse(success = true, data = token))
        }

        authenticate {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal!!.payload.getClaim("email").asString()
                val userId = principal!!.payload.getClaim("userId").asInt()
                call.respondText("Hola , $email con id $userId")
            }

        }
    }
}

