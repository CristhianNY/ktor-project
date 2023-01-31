package optimusfly.data.openai

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class OpenAI(apiKey: String) {
    private val client = OkHttpClient()
    private val apiKey: String = apiKey

    fun completion(prompt: String, maxTokens: Int): Response {

        val mediaType = "application/json".toMediaTypeOrNull()
        val body = "{\"model\":\"text-davinci-003\",\"prompt\":\"You: What have you been up to?\\nFriend: Watching old movies.\\nYou: Did you watch anything interesting?\\nFriend:\",\"temperature\":0.5,\"max_tokens\":60,\"top_p\":1.0,\"frequency_penalty\":0.5,\"presence_penalty\":0.0,\"stop\":[\"You:\"]}"
        val request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .post(RequestBody.create(mediaType, body))
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer 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")
            .build()


        return client.newCall(request).execute()
    }


}
