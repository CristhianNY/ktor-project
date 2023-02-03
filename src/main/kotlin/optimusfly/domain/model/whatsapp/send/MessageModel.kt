package optimusfly.domain.model.whatsapp.send

import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    val messaging_product: String,
    val to: String,
    val type: String,
    val template: Template
)

@Serializable
data class Template(
    val name: String,
    val language: Language
)

@Serializable
data class Language(
    val code: String
)
