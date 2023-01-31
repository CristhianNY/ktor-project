package optimusfly.domain.model.gpt.openai

import kotlinx.serialization.Serializable
import optimusfly.domain.model.gpt.response.DialogFlowResponseModel
import optimusfly.domain.model.gpt.response.FulfillmentMessage
import optimusfly.domain.model.gpt.response.Text

@Serializable
data class GptResponseModel(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)

fun GptResponseModel.toDialogFlowResponseModel(): DialogFlowResponseModel {
    return DialogFlowResponseModel(
        fulfillmentMessages = this.choices.map {
            FulfillmentMessage(
                text = Text(
                    text = listOf(it.text)
                )
            )
        }
    )
}
