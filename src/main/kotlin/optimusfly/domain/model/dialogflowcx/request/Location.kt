package optimusfly.domain.model.dialogflowcx.request

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val required: Boolean,
    val value: String
)