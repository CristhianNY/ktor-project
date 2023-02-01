package optimusfly.domain.model.dialogflowcx

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val text: Text
)