package optimusfly.domain.model.dialogflowcx.request

import kotlinx.serialization.Serializable

@Serializable
data class SessionInfo(
    val parameters: Parameters,
    val sessionId: String
)