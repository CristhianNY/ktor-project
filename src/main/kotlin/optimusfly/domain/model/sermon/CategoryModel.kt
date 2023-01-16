package optimusfly.domain.model.sermon

import kotlinx.serialization.Serializable

@Serializable
data class CategoryModel(
    val id: Int?,
    val category: String,
)