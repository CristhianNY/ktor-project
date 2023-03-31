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
                    link(
                        rel = "stylesheet",
                        href = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
                    )
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

        get("/politicas-de-privacidad") {
            call.respondHtml {
                head {
                    meta(charset = "utf-8")
                    meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
                    title { +"Políticas de Privacidad - SabioGpt" }
                    link(
                        rel = "stylesheet",
                        href = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
                    )
                    script(src = "https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js") {}
                    script(src = "https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js") {}
                    script(src = "https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js") {}
                }
                body {
                    div(classes = "container") {
                        h1 { +"Políticas de Privacidad" }
                        h2 { +"Información que recolectamos" }
                        p {
                            +"En SabioGpt, recolectamos y almacenamos información que los usuarios nos proporcionan voluntariamente al registrarse en nuestra plataforma y utilizar nuestros servicios. Esta información puede incluir, pero no se limita a:"
                        }
                        ul {
                            li { +"Nombre y apellidos" }
                            li { +"Dirección de correo electrónico" }
                            li { +"Número de teléfono" }
                            li { +"Información de uso y acceso a nuestros servicios" }
                            li { +"Cualquier otra información que el usuario decida compartir voluntariamente" }
                            h2 { +"Uso de la información" }
                            p {
                                +"Utilizamos la información recolectada para:"
                            }
                            ul {
                                li { +"Proporcionar, mantener y mejorar nuestros servicios" }
                                li { +"Personalizar la experiencia del usuario" }
                                li { +"Comunicarnos con los usuarios y responder a sus solicitudes" }
                                li { +"Cumplir con nuestras obligaciones legales y regulatorias" }
                            }

                            h2 { +"Compartir información" }
                            p {
                                +"No compartimos su información personal con terceros, excepto en los siguientes casos:"
                            }
                            ul {
                                li { +"Con su consentimiento explícito" }
                                li { +"Para cumplir con nuestras obligaciones legales y regulatorias" }
                                li { +"Para proteger y defender nuestros derechos, propiedad o seguridad, así como la de nuestros usuarios y terceros" }
                                li { +"En caso de una fusión, adquisición o venta de activos, en cuyo caso se notificará a los usuarios y se les dará la opción de eliminar su información antes de la transferencia" }
                            }

                            h2 { +"Seguridad de la información" }
                            p {
                                +"Nos esforzamos por proteger la seguridad de la información de nuestros usuarios. Implementamos medidas de seguridad físicas, administrativas y técnicas para proteger su información personal del acceso no autorizado, uso indebido, alteración, pérdida o destrucción."
                            }

                            h2 { +"Cookies y tecnologías de seguimiento" }
                            p {
                                +"Utilizamos cookies y tecnologías similares para mejorar y personalizar su experiencia en nuestra plataforma. Puede configurar su navegador para rechazar todas las cookies o indicar cuándo se está enviando una cookie. Sin embargo, algunas funciones de nuestros servicios pueden no funcionar correctamente si desactiva las cookies."
                            }

                            h2 { +"Enlaces a sitios web de terceros" }
                            p {
                                +"Nuestra plataforma puede contener enlaces a sitios web de terceros. No somos responsables de las políticas de privacidad, prácticas o contenido de dichos sitios web. Le recomendamos que revise las políticas de privacidad de cada sitio web que visite."
                            }

                            h2 { +"Cambios en nuestras políticas de privacidad" }
                            p {
                                +"Podemos actualizar nuestras políticas de privacidad periódicamente. Publicaremos cualquier cambio en esta página y, si los cambios son significativos, proporcionaremos un aviso más destacado. Le recomendamos que revise nuestras políticas de privacidad periódicamente para estar informado sobre cómo protegemos su información."
                            }
                            h2 { +"Contacto" }
                            p {
                                +"Si tiene preguntas o inquietudes sobre nuestras políticas de privacidad, no dude en ponerse en contacto con nosotros:"
                            }
                            ul {
                                li { +"Correo electrónico: soporte@sabiogpt.com" }
                                li { +"Teléfono: +1 (123) 456-7890" }
                            }
                        }
                    }
                }
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
