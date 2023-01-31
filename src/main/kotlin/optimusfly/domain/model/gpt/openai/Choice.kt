package optimusfly.domain.model.gpt.openai

import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val finish_reason: String,
    val index: Int,
    val text: String
)