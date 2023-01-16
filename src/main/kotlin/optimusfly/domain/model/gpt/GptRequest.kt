package optimusfly.domain.model.gpt

import kotlinx.serialization.Serializable

@Serializable
data class GptRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int,
    val temperature: Double,
    val top_p: Int,
    val n: Int,
    val stream: Boolean,
    val logprobs: List<String>?,
    val stop: List<String>,
    val user: String
)