package optimusfly.domain.model.gpt.response

import kotlinx.serialization.Serializable

@Serializable
data class FulfillmentMessage(
    val text: Text
)