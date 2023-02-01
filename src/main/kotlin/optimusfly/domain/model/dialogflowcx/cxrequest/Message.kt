package optimusfly.domain.model.dialogflowcx.cxrequest

data class Message(
    val responseType: String,
    val source: String,
    val text: Text
)