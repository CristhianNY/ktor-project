package optimusfly.domain.model.dialogflowcx.request

import kotlinx.serialization.Serializable

@Serializable
data class QueryTextSentiment(
    val magnitude: Double,
    val score: Double
)