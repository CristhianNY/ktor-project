package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Value(
    val contacts: List<Contact?>?,
    val messages: List<Message?>?,
    val messaging_product: String?,
    val metadata: Metadata?
)