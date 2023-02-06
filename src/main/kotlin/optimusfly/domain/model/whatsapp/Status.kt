package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val id: String,
    val status: String,
    val timestamp: String,
    val recipient_id: String,
    val conversation: Conversation,
    val pricing: Pricing
)