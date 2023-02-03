package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val from: String?,
    val id: String?,
    val text: Text?,
    val timestamp: String?,
    val type: String?
)