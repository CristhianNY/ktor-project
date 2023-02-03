package optimusfly.domain.model.whatsapp.send

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val input: String,
    val wa_id: String
)