package optimusfly.domain.model.error

import kotlinx.serialization.Serializable

@Serializable
data class GenericError<T>(
    val data: T,
    val success: Boolean,
    val customErrorCode: Int
)