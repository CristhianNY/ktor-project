package optimusfly.domain.model.gpt

import kotlinx.serialization.Serializable

@Serializable
data class GptRequest(
    val frequencyPenalty: Double,
    val maxTokens: Int,
    val model: String,
    val presencePenalty: Double,
    val prompt: String? = null,
    val stop: List<String>,
    val temperature: Double,
    val topP: Int,
    val user: String
)