package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Text(
    val body: String?
)