package optimusfly.domain.model.whatsapp.send

import kotlinx.serialization.Serializable

@Serializable
data class Reaction(
    val message_id: String,
    val emoji: String
)