package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Change(
    val field : String?,
    val value: Value?
)