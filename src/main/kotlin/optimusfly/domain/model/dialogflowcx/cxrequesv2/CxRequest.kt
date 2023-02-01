package optimusfly.domain.model.dialogflowcx.cxrequesv2

import kotlinx.serialization.Serializable

@Serializable
data class CxRequest(
    val detectIntentResponseId: String,
    val fulfillmentInfo: FulfillmentInfo,
    val languageCode: String,
    val messages: List<Message>,
    val pageInfo: PageInfo,
    val sessionInfo: SessionInfo,
    val text: String
)