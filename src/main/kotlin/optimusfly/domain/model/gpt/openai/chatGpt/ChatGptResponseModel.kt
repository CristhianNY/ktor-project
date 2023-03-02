package optimusfly.domain.model.gpt.openai.chatGpt

import kotlinx.serialization.Serializable

@Serializable
data class ChatGptResponseModel(
    val id: String,
    val `object`: String,
    val created: Long,
    val choices: List<Choice>,
    val usage: Usage
)

@Serializable
data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String
)

@Serializable
data class Message(
    val role: String,
    val content: String
)
@Serializable
data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)
