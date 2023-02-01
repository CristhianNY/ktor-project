package optimusfly.domain.model.dialogflowcx.cxrequest

import kotlinx.serialization.Serializable

@Serializable
data class IntentInfo(
    val confidence: Double?,
    val displayName: String?,
    val lastMatchedIntent: String?
)