package optimusfly

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import optimusfly.data.user.UserEntity
import optimusfly.plugins.configureRouting
import optimusfly.plugins.userModule
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    userModule()
    install(ContentNegotiation){
        json()
    }

    val database = Database.connect(url = "jdbc:mysql://localhost:3306/sermons",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "")

    /**database.insert(UserEntity){
        set(it.name,"Cristhian")
        set(it.email, "cristhian4545@gmail.com")
        set(it.password, "password1")
        set(it.lastName, "Bonilla")
    }**/

    database.update(UserEntity){
        set(it.name,"Cris")
        where {
            it.id eq  1
        }
    }

    /* delete
    database.delete(UserEntity){
        it.id eq 2
    }*/

    var users = database.from(UserEntity).select()
    for(row in users){
        println("${row[UserEntity.id]}: ${row[UserEntity.name]}")
    }
}
