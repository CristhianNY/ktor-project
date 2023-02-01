package optimusfly.domain.model.dialogflowcx.cxrequestv3

import kotlinx.serialization.Serializable

@Serializable
data class SessionInfo(
    val session: String
)