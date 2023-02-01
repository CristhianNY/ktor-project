package optimusfly.domain.model.dialogflowcx.cxrequest

import kotlinx.serialization.Serializable

@Serializable
data class Text(
    val redactedText: List<String>,
    val text: List<String>
)