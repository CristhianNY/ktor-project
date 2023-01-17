package optimusfly.domain.model.sermon

import kotlinx.serialization.Serializable

@Serializable
data class SermonRequest(
    val category: Int?,
    val sermon: String
)