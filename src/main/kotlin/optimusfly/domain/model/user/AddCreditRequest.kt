package optimusfly.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class AddCreditRequest(val credit: Int)