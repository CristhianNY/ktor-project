package optimusfly.domain.model.whatsapp.send

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponseModel(
    val contacts: List<Contact?>?,
    val messages: List<Message?>?,
    val messaging_product: String?
)