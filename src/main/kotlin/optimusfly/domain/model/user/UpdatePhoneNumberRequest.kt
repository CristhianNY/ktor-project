package optimusfly.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePhoneNumberRequest(val newPhoneNumber: String)
