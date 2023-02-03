package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable

@Serializable
data class WebHookPetitionModel(
    val entry: List<Entry?>?,
    val `object`: String?
)