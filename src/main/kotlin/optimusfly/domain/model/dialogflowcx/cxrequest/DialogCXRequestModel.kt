package optimusfly.domain.model.dialogflowcx.cxrequest

import kotlinx.serialization.Serializable

@Serializable
data class DialogCXRequestModel(
    val detectIntentResponseId: String?,
    val fulfillmentInfo: FulfillmentInfo?,
    val intentInfo: IntentInfo?,
    val languageCode: String?,
    val messages: List<Message>?,
    val pageInfo: PageInfo?,
    val sessionInfo: SessionInfo?,
    val text: String?
)