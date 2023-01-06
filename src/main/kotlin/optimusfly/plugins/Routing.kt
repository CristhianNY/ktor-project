package optimusfly.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import java.io.File

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/hola") {
            call.respondText {
                "Hola Cristhian"
            }
        }

        get("/fileDownload") {
            val file = File("./files/saray.jpeg")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(
                    ContentDisposition.Parameters.FileName,
                    "dowloadingImage.jpeg"
                ).toString()
            )
            call.respondFile(file)
        }
    }
}
