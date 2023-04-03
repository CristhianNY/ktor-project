package optimusfly.domain.model.user

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserCredentialsWithToken(
    val name: String?,
    val lastName: String?,
    val email: String?,
    val idToken: String?
) {
    fun hashedIdToken(): String {
        return BCrypt.hashpw(idToken, BCrypt.gensalt())
    }
}