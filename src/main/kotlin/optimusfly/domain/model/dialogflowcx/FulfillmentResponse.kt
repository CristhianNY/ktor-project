package optimusfly.domain.model.dialogflowcx

import kotlinx.serialization.Serializable

@Serializable
data class FulfillmentResponse(
    val messages: List<Message>
)