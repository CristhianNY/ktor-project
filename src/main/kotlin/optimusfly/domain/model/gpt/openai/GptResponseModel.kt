package optimusfly.domain.model.gpt.openai

import kotlinx.serialization.Serializable
import optimusfly.domain.model.dialogflowcx.DialogFlowResponseCXModel
import optimusfly.domain.model.dialogflowcx.FulfillmentResponse
import optimusfly.domain.model.dialogflowcx.Message
import optimusfly.domain.model.dialogflowcx.Text

@Serializable
data class GptResponseModel(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)

fun GptResponseModel.toDialogFlowResponseCXModel(): DialogFlowResponseCXModel {
    return DialogFlowResponseCXModel(
        fulfillmentResponse = FulfillmentResponse(
            messages = listOf(
                Message(
                    text = Text(
                        text = listOf(this.choices.first().text)
                    )
                )
            )
        )
    )
}
