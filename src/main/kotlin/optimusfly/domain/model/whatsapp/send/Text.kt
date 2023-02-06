package optimusfly.domain.model.whatsapp.send

import kotlinx.serialization.Serializable

@Serializable
data class Text(
    val preview_url: Boolean,
    val body: String
)