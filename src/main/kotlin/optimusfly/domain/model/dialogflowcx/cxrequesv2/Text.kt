package optimusfly.domain.model.dialogflowcx.cxrequesv2

import kotlinx.serialization.Serializable

@Serializable
data class Text(
    val redactedText: List<String>,
    val text: List<String>
)