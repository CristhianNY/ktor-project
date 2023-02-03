package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Contact(
    val profile: Profile?,
    val wa_id: String?
)