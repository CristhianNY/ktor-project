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
            .url("https://graph.facebook.com/v15.0/129746563358123/messages")
            .post(RequestBody.create(mediaType, body))
            .addHeader("Content-Type", "application/json")
            .addHeader(
                "Authorization",
                "Bearer EAAMZBu7GdAScBAJrZCFfk6jbk1B7VHLySFlPZAV9CYYHcZCvNbfUhqmPoERH9fZAjDZAKM3b7vdZCZA4ZCWu8ZC3IZBB15rxn8TDqmPEaTxRwTzfpsO1VILbJAn9ACZCYS5rfDy0587t0sfkAiDAkcb2BFl1ojZCryYKnJhH4hpavuoS3qFCo0ZBY6JYRzKMJ5A5GTZCV1Srvta3wNMqQZDZD"
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
                "Bearer EAAMZBu7GdAScBAJrZCFfk6jbk1B7VHLySFlPZAV9CYYHcZCvNbfUhqmPoERH9fZAjDZAKM3b7vdZCZA4ZCWu8ZC3IZBB15rxn8TDqmPEaTxRwTzfpsO1VILbJAn9ACZCYS5rfDy0587t0sfkAiDAkcb2BFl1ojZCryYKnJhH4hpavuoS3qFCo0ZBY6JYRzKMJ5A5GTZCV1Srvta3wNMqQZDZD"
            )
            .build()

        client.newCall(request).execute().use { response ->
            response.body?.close()
            return response
        }
    }
}
