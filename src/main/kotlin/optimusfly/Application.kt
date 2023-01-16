package optimusfly

import com.typesafe.config.ConfigFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.cio.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import optimusfly.plugins.configureRouting
import optimusfly.plugins.sermonModule
import optimusfly.plugins.userModule
import optimusfly.utils.TokenManager

fun main() {
    embeddedServer(CIO, port = System.getenv("PORT").toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    setUpJWTAuthentication(this)
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
    userModule()
   // sermonModule()
}


fun setUpJWTAuthentication(application: Application) {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)
    application.install(Authentication) {
        jwt {
            verifier(tokenManager.verifyJWTToken())
            realm = config.property("realm").getString()
            validate { jwtCredential ->
                if (jwtCredential.payload.getClaim("email").asString().isNotEmpty()) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
        }
    }
}
