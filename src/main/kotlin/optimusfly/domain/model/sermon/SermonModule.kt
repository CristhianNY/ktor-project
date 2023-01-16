package optimusfly.domain.model.sermon

import kotlinx.serialization.Serializable

@Serializable
data class SermonModule(
    val userId: Int?,
    val userName: String?,
    val sermon: String
)