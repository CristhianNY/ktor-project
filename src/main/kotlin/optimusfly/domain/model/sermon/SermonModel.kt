package optimusfly.domain.model.sermon

import kotlinx.serialization.Serializable

@Serializable
data class SermonModel(
    val userId: Int?,
    val sermon: String,
    val category: String?
)