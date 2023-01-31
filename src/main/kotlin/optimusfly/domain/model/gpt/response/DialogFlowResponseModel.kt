package optimusfly.domain.model.gpt.response

import kotlinx.serialization.Serializable

@Serializable
data class DialogFlowResponseModel(
    val fulfillmentMessages: List<FulfillmentMessage>
)