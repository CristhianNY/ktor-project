package optimusfly.domain.model.dialogFlowRequest

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParametersX(
    @SerialName("param-name")
    val paramMame: String
)