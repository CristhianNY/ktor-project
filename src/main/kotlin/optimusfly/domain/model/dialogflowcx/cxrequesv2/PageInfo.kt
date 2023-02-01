package optimusfly.domain.model.dialogflowcx.cxrequesv2

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    val currentPage: String,
    val displayName: String
)