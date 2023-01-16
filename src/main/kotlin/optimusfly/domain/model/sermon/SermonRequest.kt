package optimusfly.domain.model.sermon

import kotlinx.serialization.Serializable

@Serializable
data class SermonRequest(
    val userId: Int?,
    val userName: String?,
    val sermon: String,
    val category: Int?
)