package com.example.reto11

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response

data class ChatRequest(val model: String, val messages: List<Map<String, String>>)
data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Map<String, String>)

interface OpenAIApi {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    fun sendMessage(@Body request: ChatRequest): Call<ChatResponse>
}

class MainActivity : AppCompatActivity() {
    private lateinit var chatOutput: TextView
    private lateinit var userInput: EditText
    private lateinit var sendButton: Button
    private lateinit var openAIApi: OpenAIApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatOutput = findViewById(R.id.chatOutput)
        userInput = findViewById(R.id.userInput)
        sendButton = findViewById(R.id.sendButton)

        // Configurar Retrofit
        val apiKey = "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"
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $apiKey")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        openAIApi = retrofit.create(OpenAIApi::class.java)

        sendButton.setOnClickListener {
            val userMessage = userInput.text.toString()
            if (userMessage.isNotEmpty()) {
                sendMessageToChatbot(userMessage)
                userInput.text.clear()
            }
        }
    }

    private fun sendMessageToChatbot(message: String) {
        val request = ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(mapOf("role" to "user", "content" to message))
        )

        openAIApi.sendMessage(request).enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    val reply = response.body()?.choices?.firstOrNull()?.message?.get("content")
                    chatOutput.append("Tú: $message\n")
                    chatOutput.append("Bot: $reply\n\n")
                } else {
                    chatOutput.append("Error: ${response.errorBody()?.string()}\n")
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                chatOutput.append("Error de red: ${t.message}\n")
            }
        })
    }
}