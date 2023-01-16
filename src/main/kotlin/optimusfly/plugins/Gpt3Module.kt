package optimusfly.plugins

import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import optimusfly.domain.model.gpt.GptRequest

fun Application.gpt3Module() {
    routing {
        authenticate {
            post("get-sermons") {
                val request = call.receive<GptRequest>()
               //  val client = HttpClient()
                // Establecer la URL de la API de GPT-3 y la clave de API
                val apiUrl = "https://api.openai.com/v1/completions"
                val apiKey = "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"

                call.respondText("Hola , $request")
            }
        }
    }
}