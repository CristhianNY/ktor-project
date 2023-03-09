package optimusfly.domain.model.whatsapp.send_welcome

import com.google.gson.Gson
import kotlinx.serialization.Serializable

@Serializable
data class MessageTemplate(
    val messaging_product: String,
    val to: String,
    val type: String,
    val template: FacebookTemplate
){
    fun toJson(): String = Gson().toJson(this)
}

@Serializable
data class FacebookTemplate(
    val name: String,
    val language: FacebookLanguage
)

@Serializable
data class FacebookLanguage(
    val code: String
)