package optimusfly.domain.model.dialogflowcx.request

import kotlinx.serialization.Serializable

@Serializable
data class DialogFlowCXRequestModel(
    val detectIntentResponseId: String,
    val fulfillmentInfo: FulfillmentInfo,
    val intentInfo: IntentInfo,
    val languageCode: String,
    val messages: List<Message>,
    val pageInfo: PageInfo,
    val payload: Payload,
    val sentimentAnalysisResult: SentimentAnalysisResult,
    val sessionInfo: SessionInfo,
    val text: String
)