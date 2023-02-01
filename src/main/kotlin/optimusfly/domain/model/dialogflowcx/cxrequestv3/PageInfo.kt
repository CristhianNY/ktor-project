package optimusfly.domain.model.dialogflowcx.cxrequestv3

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    val currentPage: String,
    val displayName: String
)