package optimusfly.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class PhoneNumberRequest(val phoneNumber: String)