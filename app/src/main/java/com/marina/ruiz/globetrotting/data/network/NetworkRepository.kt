package com.marina.ruiz.globetrotting.data.network

import com.marina.ruiz.globetrotting.data.network.chatGpt.ChatGptApiService
import com.marina.ruiz.globetrotting.data.network.chatGpt.model.ChatGptResponse
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.RickAndMortyApiService
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.CharacterApiModel
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.LocationApiModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val service: RickAndMortyApiService,
    private val chatGpt: ChatGptApiService
) {

    suspend fun getAllCharacters(): List<CharacterApiModel> {
        val response = service.api.getAllCharacters()
        return response.results.map {
            it.asApiModel()
        }
    }

    suspend fun getAllLocations(): List<LocationApiModel> {
        val response = service.api.getAllLocations()
        return response.results.map { result ->
            result.asApiModel()
        }
    }

    suspend fun getShortDescription(destination: String): String {
        val requestBody =
            createRequestBody("Crea una descripción corta (10 palabras máximo) sobre el destino de viaje '${destination}' basado en la serie Rick & Morty. No digas que es ficticio")
        val response: ChatGptResponse = chatGpt.api.getResponse(requestBody)
        return response.asApiModel().text.trim().replace("\"", "")
    }

    suspend fun getLongDescription(destination: String): String {
        val requestBody =
            createRequestBody("Crea una descripción (100 palabras en 2 o 3 párrafos) sobre el destino de viaje '${destination}' basado en la serie Rick & Morty. No digas que es ficticio")
        val response: ChatGptResponse = chatGpt.api.getResponse(requestBody)
        return response.asApiModel().text.trim()
    }

    private fun createRequestBody(request: String): RequestBody {
        val json = JSONObject()
        json.put("model", "gpt-3.5-turbo-instruct")
        json.put("prompt", request)
        json.put("max_tokens", 250)
        json.put("temperature", 0.7)

        val mediaType = "application/json; charset=utf-8".toMediaType()
        return json.toString().toRequestBody(mediaType)
    }
}