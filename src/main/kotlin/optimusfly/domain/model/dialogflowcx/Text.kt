package optimusfly.domain.model.dialogflowcx

import kotlinx.serialization.Serializable

@Serializable
data class Text(
    val text: List<String>
)