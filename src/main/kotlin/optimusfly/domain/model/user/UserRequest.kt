package optimusfly.domain.model.user

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt


@Serializable
data class UserRequest(val name: String?, val lastName: String?, val email: String?, val password: String?){

    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}