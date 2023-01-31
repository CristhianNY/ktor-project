package optimusfly.domain.model.gpt

import kotlinx.serialization.Serializable

@Serializable
data class Text(
    val text: List<String>
)