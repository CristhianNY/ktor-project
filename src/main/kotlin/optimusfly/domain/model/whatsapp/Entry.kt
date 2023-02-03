package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Entry(
    val changes: List<Change?>?,
    val id: String
)