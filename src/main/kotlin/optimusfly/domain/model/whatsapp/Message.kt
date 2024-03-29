package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val from: String,
    val id: String,
    val timestamp: String,
    val text: Text,
    val type: String
)