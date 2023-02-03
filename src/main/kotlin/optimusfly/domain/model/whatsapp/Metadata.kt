package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val display_phone_number: String?,
    val phone_number_id: String?
)