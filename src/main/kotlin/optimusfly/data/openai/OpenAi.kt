package optimusfly.data.openai

import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class OpenAI(apiKey: String) {
    private val client = OkHttpClient()
    private val apiKey: String = apiKey

    fun completion(prompt: String, maxTokens: Int): Response {

        val mediaType = "application/json".toMediaTypeOrNull()
        val body =
            "{\"model\":\"text-davinci-003\",\"prompt\":\"$prompt\\nFriend:\",\"temperature\":0.5,\"max_tokens\":60,\"top_p\":1.0,\"frequency_penalty\":0.5,\"presence_penalty\":0.0,\"stop\":[\"You:\"]}"
        val request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .post(RequestBody.create(mediaType, body))
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "")
            .build()


        return client.newCall(request).execute()
    }


    fun chatCompletion(model: String, messages: List<Map<String, String>>): Response {
        val mediaType = "application/json".toMediaTypeOrNull()
        val json = "{\"model\":\"$model\",\"messages\":${toJson(messages)}}"
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(RequestBody.create(mediaType, json))
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "")
            .build()

        return client.newCall(request).execute()
    }

    private fun toJson(obj: Any): String {
        return GsonBuilder().create().toJson(obj)
    }

}
