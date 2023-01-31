package optimusfly.domain.model.dialogFlowRequest

import kotlinx.serialization.Serializable

@Serializable
data class Intent(
    val displayName: String,
    val name: String
)