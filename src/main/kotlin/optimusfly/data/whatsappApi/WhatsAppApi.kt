package optimusfly.data.whatsappApi

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import optimusfly.domain.model.whatsapp.send.MessageToSendModel

class WhatsAppApi(val bearerToken: String) {
    private val client = OkHttpClient()

    fun sendMessage(message: MessageToSendModel): Response {

        val mediaType = "application/json".toMediaTypeOrNull()

        val body = message.toJson()
        val request = Request.Builder()
            .url("https://graph.facebook.com/v15.0/116126038051629/messages")
            .post(RequestBody.create(mediaType, body))
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        client.newCall(request).execute().use { response ->
            response.body?.close()
            return response
        }
    }
}
