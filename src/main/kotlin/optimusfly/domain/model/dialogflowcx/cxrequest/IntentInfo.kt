package optimusfly.domain.model.dialogflowcx.cxrequest

data class IntentInfo(
    val confidence: Double,
    val displayName: String,
    val lastMatchedIntent: String
)