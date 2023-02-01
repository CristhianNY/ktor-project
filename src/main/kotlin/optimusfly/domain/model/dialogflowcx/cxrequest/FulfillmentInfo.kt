package optimusfly.domain.model.dialogflowcx.cxrequest

import kotlinx.serialization.Serializable

@Serializable
data class FulfillmentInfo(
    val tag: String?
)