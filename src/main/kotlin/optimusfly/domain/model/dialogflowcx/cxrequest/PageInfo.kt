package optimusfly.domain.model.dialogflowcx.cxrequest

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    val currentPage: String?,
    val displayName: String?
)