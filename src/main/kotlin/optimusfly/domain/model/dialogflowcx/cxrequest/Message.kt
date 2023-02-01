package optimusfly.domain.model.dialogflowcx.cxrequest

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val responseType: String?,
    val source: String?,
    val text: Text?
)