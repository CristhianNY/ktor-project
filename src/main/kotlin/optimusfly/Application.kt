package optimusfly

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import optimusfly.plugins.configureRouting
import optimusfly.plugins.userModule
import org.ktorm.database.Database

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
}
