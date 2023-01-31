package optimusfly.domain.model.gpt.response

import kotlinx.serialization.Serializable

@Serializable
data class Text(
    val text: List<String>
)