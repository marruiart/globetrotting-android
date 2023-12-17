package com.marina.ruiz.globetrotting.data.network.chatGpt

import com.marina.ruiz.globetrotting.data.network.chatGpt.model.ChatGptResponse
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface ChatGptApi {
    @Headers("Authorization: Bearer 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")
    @POST("v1/completions")
    suspend fun getResponse(@Body requestBody: RequestBody): ChatGptResponse
}

@Singleton
class ChatGptApiService @Inject constructor() {
    // Instantiate retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create API
    val api: ChatGptApi = retrofit.create(ChatGptApi::class.java)
}