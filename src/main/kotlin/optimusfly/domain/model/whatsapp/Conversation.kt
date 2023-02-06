package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(val id: String, val origin: Origin)