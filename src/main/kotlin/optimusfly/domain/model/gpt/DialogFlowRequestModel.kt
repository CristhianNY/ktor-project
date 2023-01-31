package optimusfly.domain.model.gpt

import kotlinx.serialization.Serializable

@Serializable
data class DialogFlowRequestModel(
    val originalDetectIntentRequest: OriginalDetectIntentRequest,
    val queryResult: QueryResult,
    val responseId: String,
    val session: String
)