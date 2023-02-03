package optimusfly.domain.model.whatsapp.get

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMessageSuccess(
    val data: String,
    val success: Boolean
)