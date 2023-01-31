package optimusfly.domain.model.dialogFlowRequest

import kotlinx.serialization.Serializable

@Serializable
data class FulfillmentMessage(
    val text: Text
)