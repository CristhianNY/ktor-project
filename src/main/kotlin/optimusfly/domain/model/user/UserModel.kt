package optimusfly.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: Int?,
    val name: String?,
    val lastName: String?,
    val email: String?,
    val password: String?,
    val subscription: Int?,
    val phoneNumber: String?,
    val credit: Int?
)