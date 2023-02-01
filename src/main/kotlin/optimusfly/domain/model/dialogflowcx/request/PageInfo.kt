package optimusfly.domain.model.dialogflowcx.request

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    val currentPage: String,
    val formInfo: FormInfo
)