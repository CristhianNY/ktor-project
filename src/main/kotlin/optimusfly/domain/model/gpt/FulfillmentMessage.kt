package optimusfly.domain.model.gpt

import kotlinx.serialization.Serializable

@Serializable
data class FulfillmentMessage(
    val text: Text
)