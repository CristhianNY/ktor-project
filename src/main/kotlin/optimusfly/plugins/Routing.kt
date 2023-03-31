package optimusfly.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.head
import kotlinx.html.*
import java.io.File

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondHtml {
                head {
                    meta(charset = "utf-8")
                    meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
                    title { +"SabioGpt - Integración de WhatsApp con ChatGPT" }
                    link(rel = "stylesheet", href = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css")
                    script(src = "https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js") {}
                    script(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js") {}
                    script(src = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js") {}
                }
                body {
                    div(classes = "jumbotron") {
                        h1 { +"SabioGpt" }
                        p { +"Integración de WhatsApp con ChatGPT" }
                    }
                    div(classes = "container") {
                        h2 { +"Característicasde SabioGpt" }
                        ul {
                            li { +"Comunicación fluida con ChatGPT a través de WhatsApp." }
                            li { +"Respuestas rápidas y precisas gracias a la IA de ChatGPT." }
                            li { +"Configuración sencilla y rápida para comenzar a usar la aplicación." }
                            li { +"Interfaz de usuario intuitiva y fácil de usar." }
                            li { +"Soporte para múltiples idiomas y conversaciones simultáneas." }
                            li { +"Potente análisis de texto para proporcionar respuestas adecuadas a preguntas complejas." }
                        }
                        h2 { +"¿Cómo comenzar?" }
                        p { +"Para comenzar a utilizar SabioGpt, siga estos simples pasos:" }
                        ol {
                            li { +"Regístrese en nuestra plataforma y descargue la aplicación SabioGpt." }
                            li { +"Instale la aplicación en su dispositivo e ingrese sus credenciales de acceso." }
                            li { +"Conecte su cuenta de WhatsApp siguiendo las instrucciones proporcionadas en la aplicación." }
                            li { +"Comience a disfrutar de la integración de ChatGPT en sus conversaciones de WhatsApp." }
                        }
                    }
                    div(classes = "container mt-5") {
                        p { +"Si necesita ayuda o tiene preguntas, no dude en ponerse en contacto con nuestro equipo de soporte." }
                    }
                }
            }
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
