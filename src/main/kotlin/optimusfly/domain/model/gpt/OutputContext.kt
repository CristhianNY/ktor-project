package optimusfly.domain.model.gpt

import kotlinx.serialization.Serializable

@Serializable
data class OutputContext(
    val lifespanCount: Int,
    val name: String,
    val parameters: ParametersX
)