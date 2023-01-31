package optimusfly.domain.model.dialogFlowRequest

import kotlinx.serialization.Serializable

@Serializable
data class QueryResult(
    val allRequiredParamsPresent: Boolean,
    val diagnosticInfo: DiagnosticInfo,
    val fulfillmentMessages: List<FulfillmentMessage>,
    val fulfillmentText: String,
    val intent: Intent,
    val intentDetectionConfidence: Int,
    val languageCode: String,
    val outputContexts: List<OutputContext>,
    val parameters: ParametersX,
    val queryText: String
)