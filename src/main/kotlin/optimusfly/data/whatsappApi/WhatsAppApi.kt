package optimusfly.data.whatsappApi

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import optimusfly.domain.model.whatsapp.send.MessageToSendModel
import optimusfly.domain.model.whatsapp.send_welcome.MessageTemplate

class WhatsAppApi {
    private val client = OkHttpClient()

    fun sendMessage(message: MessageToSendModel): Response {

        val mediaType = "application/json".toMediaTypeOrNull()

        val body = message.toJson()
        val request = Request.Builder()
            .url("https://graph.facebook.com/v15.0/116126038051629/messages")
            .post(RequestBody.create(mediaType, body))
            .addHeader("Content-Type", "application/json")
            .addHeader(
                "Authorization",
                "Bearer EAAMZBu7GdAScBAOCTFnZCqo5qk6Rfnp5rpi6UdxVycNCfCSmZBBJXifUxaxZAUz9M5kvmUUzUW7EVSLGDXCyNKtSxtCXb64DGwsZCThvpLfCodUlg95e9x2xpGH6wR68a1CCKV29dtiG0PGxMuZCI2Y7Ev7Q3wEgM5sbNIx09j8vefNrXISvEZCbSNwq6e8X2P81sWOnq6od0WAYxh0ThPV"
            )
            .build()

        client.newCall(request).execute().use { response ->
            response.body?.close()
            return response
        }
    }

    fun sendMessageTemplate(message: MessageTemplate): Response {
        val mediaType = "application/json".toMediaTypeOrNull()
        val body = message.toJson()
        val request = Request.Builder()
            .url("https://graph.facebook.com/v15.0/116126038051629/messages")
            .post(RequestBody.create(mediaType, body))
            .addHeader("Content-Type", "application/json")
            .addHeader(
                "Authorization",
                "Bearer EAAMZBu7GdAScBAOCTFnZCqo5qk6Rfnp5rpi6UdxVycNCfCSmZBBJXifUxaxZAUz9M5kvmUUzUW7EVSLGDXCyNKtSxtCXb64DGwsZCThvpLfCodUlg95e9x2xpGH6wR68a1CCKV29dtiG0PGxMuZCI2Y7Ev7Q3wEgM5sbNIx09j8vefNrXISvEZCbSNwq6e8X2P81sWOnq6od0WAYxh0ThPV"
            )
            .build()

        client.newCall(request).execute().use { response ->
            response.body?.close()
            return response
        }
    }
}
