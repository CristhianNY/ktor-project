package optimusfly.domain.model.dialogflowcx.request

import kotlinx.serialization.Serializable

@Serializable
data class Google(
    val expectUserResponse: Boolean
)