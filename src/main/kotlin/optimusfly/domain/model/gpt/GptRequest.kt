package optimusfly.domain.model.gpt

import kotlinx.serialization.Serializable

@Serializable
data class GptRequest(
    val frequencyPenalty: Double?,
    val max_tokens: Int?,
    val model: String?,
    val presence_penalty: Double?,
    val prompt: String? = null,
    val stop: List<String>?,
    val temperature: Double?,
    val top_p: Int?,
    val user: String?
)