package optimusfly.domain.model.whatsapp.send

import com.google.gson.Gson
import kotlinx.serialization.Serializable

@Serializable
data class MessageToSendModel(
    val messaging_product: String,
    val to: String,
    val type: String,
    val text: Text? = null,
    val reaction: Reaction?= null,
    val image: Image?= null,
    val location: Location?= null

){
    fun toJson(): String = Gson().toJson(this)
}