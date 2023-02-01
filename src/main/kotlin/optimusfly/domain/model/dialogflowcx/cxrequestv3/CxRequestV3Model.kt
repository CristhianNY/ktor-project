package optimusfly.domain.model.dialogflowcx.cxrequestv3

import kotlinx.serialization.Serializable

@Serializable
data class CxRequestV3Model(
    val detectIntentResponseId: String?,
    val fulfillmentInfo: FulfillmentInfo?,
    val languageCode: String?,
    val pageInfo: PageInfo?,
    val sessionInfo: SessionInfo?,
    val text: String?
)