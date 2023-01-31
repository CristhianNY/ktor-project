package optimusfly.domain.model.gpt

import kotlinx.serialization.Serializable

@Serializable
data class Intent(
    val displayName: String,
    val name: String
)