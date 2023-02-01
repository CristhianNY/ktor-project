package optimusfly.domain.model.dialogflowcx

import kotlinx.serialization.Serializable

@Serializable
data class DialogFlowResponseCXModel(
    val fulfillmentResponse: FulfillmentResponse
)