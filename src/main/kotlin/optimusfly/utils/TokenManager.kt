package optimusfly.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import optimusfly.bff.model.UserModel
import java.util.*

class TokenManager(val config: HoconApplicationConfig) {
    private val audience = config.property("audience").getString()
    private val secret = config.property("secret").getString()
    private val issuer = config.property("issuer").getString()
    private val expirationDate = System.currentTimeMillis() + 6000000

    fun generateJWTToken(user: UserModel): String {
        val token = JWT.create().withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", user.email)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(secret))

        return token
    }

    fun verifyJWTToken() : JWTVerifier{
        return JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }
}