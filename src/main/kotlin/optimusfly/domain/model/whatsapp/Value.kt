package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class Value(
    val messaging_product: String,
    val metadata: Metadata,
    val contacts: List<Contact>?,
    val messages: List<Message>?,
    val statuses: List<Status>?
)