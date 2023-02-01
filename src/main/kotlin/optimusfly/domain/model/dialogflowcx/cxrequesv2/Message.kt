package optimusfly.domain.model.dialogflowcx.cxrequesv2

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val responseType: String,
    val source: String,
    val text: Text
)