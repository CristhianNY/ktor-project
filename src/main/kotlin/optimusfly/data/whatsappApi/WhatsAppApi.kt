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
                "Bearer EAAMZBu7GdAScBAL4eoCGZAL3QjNTahO59u8Q7fHsPJngGaYNlAZBJPnItWLuKKqC2OqPxPUZBQ4l6onqla5cgO9fsBQMnJLZCD4oI71gfZBOOnm9UjafSFIWS2ko0ZA2cUGJLKGLKZBTbm6UwSreke0Jp9NWj0kmIktU1VNscRFCM1u2pJZBrnZA9PFhskpuRSuHBZAmLInGX6zBvvOe5vA7Jmr"
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
                "Bearer EAAMZBu7GdAScBAJRRCDnsZCcJAoYvh4r8lObLf2wxdtpzpQUOMhSswe7o6Ng2HfZBH3ZBAQzWK9RsTQmkQXWtlrNo3jZC2FAIOIluyJymmUcYDNqkMkOp2z74qZCm5pjIf4Y06c3k1Jq7RLAu8UYk4bUz0B8kxYoJSMRUqimHZB4VOD5gyZAwX19rilviEc0RKfJLBrYJ60RDLVR3u2sJ9PA"
            )
            .build()

        client.newCall(request).execute().use { response ->
            response.body?.close()
            return response
        }
    }
}
