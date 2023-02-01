package optimusfly.domain.model.dialogflowcx.request

import kotlinx.serialization.Serializable

@Serializable
data class FulfillmentInfo(
    val fulfillmentMessages: List<FulfillmentMessage>
)