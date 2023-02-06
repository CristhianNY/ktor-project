package optimusfly.domain.model.whatsapp.send

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val longitude: Double,
    val latitude: Double,
    val name: String,
    val address: String
)