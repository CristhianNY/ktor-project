package optimusfly.data.openai

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class OpenAI(apiKey: String) {
    private val client = OkHttpClient()
    private val apiKey: String = apiKey

    fun completion(prompt: String, maxTokens: Int): Response {
        val request = Request.Builder()
            .url("https://api.openai.com/v1/engines/curie/completions")
            .post(
                """
               {
                "model": "text-davinci-003",
                 "prompt": "$prompt",
                 "max_tokens": $maxTokens,
                "temperature": 0.9,
                "top_p": 1,
                "n": 1,
                "stream": false,
                "logprobs": null,
                 "stop": "\n"
               }
            """.trimIndent().toRequestBody("application/json".toMediaType())
            )
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        return client.newCall(request).execute()
    }


}
