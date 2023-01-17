package optimusfly.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import optimusfly.data.db.DatabaseConnection
import optimusfly.data.sermons.CategoryEntity
import optimusfly.data.sermons.SermonEntity
import optimusfly.data.sermons.SermonEntity.categoryId
import optimusfly.data.sermons.SermonEntity.sermonContent
import optimusfly.data.user.UserEntity
import optimusfly.domain.model.sermon.CategoryModel
import optimusfly.domain.model.sermon.SermonModel
import optimusfly.domain.model.sermon.SermonRequest
import optimusfly.domain.model.user.UserResponse
import org.ktorm.dsl.*

const val SUCCESS_INSERT_SERMON = 1
fun Application.sermonModule() {
    val db = DatabaseConnection.database

    routing {
        authenticate {
            post("save-sermon") {

                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asInt()

                val request = call.receive<SermonRequest>()
                val result = db.insert(SermonEntity) {
                    set(it.sermonContent, request.sermon)
                    set(it.userId, userId)
                    set(it.categoryId, request.category)
                }

                if (result == SUCCESS_INSERT_SERMON) {
                    call.respond(
                        HttpStatusCode.OK, UserResponse(success = true, data = "sermon saved successfully")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest, UserResponse(success = true, data = "Error Inserting Sermon")
                    )
                }
            }

            get("get-categories") {
                val categories: List<CategoryModel> = db.from(CategoryEntity).select().map {
                    val id = it[CategoryEntity.id]
                    val category = it[CategoryEntity.name]

                    CategoryModel(id, category.orEmpty())

                }
                call.respond(categories)
            }

            get("get-all-sermons") {
                val sermons: List<SermonModel> = db.from(SermonEntity).select().map {
                    val id = it[SermonEntity.id]
                    val sermonContent = it[SermonEntity.sermonContent]
                    val categoryId = it[SermonEntity.categoryId]

                    SermonModel(id, sermonContent.orEmpty(), categoryId)

                }
                call.respond(sermons)
            }

            get("get-sermon-by-id") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal!!.payload.getClaim("userId").asInt()

                val sermon: List<SermonModel> =
                    db.from(SermonEntity).select().where { SermonEntity.userId eq userId }.map {
                        val id = it[SermonEntity.id]
                        val sermonContent = it[SermonEntity.sermonContent]
                        val categoryId = it[SermonEntity.categoryId]

                        SermonModel(id, sermonContent.orEmpty(), categoryId)

                    }

                call.respond(sermon)
            }

        }
    }
}