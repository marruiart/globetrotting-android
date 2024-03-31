package com.marina.ruiz.globetrotting.data.network

import android.util.Log
import androidx.lifecycle.distinctUntilChanged
import com.marina.ruiz.globetrotting.data.network.chatGpt.ChatGptApiService
import com.marina.ruiz.globetrotting.data.network.chatGpt.model.ChatGptResponse
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.UserDataResponse
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.RickAndMortyApiService
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.CharacterApiModel
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.LocationApiModel
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val rickAndMortySvc: RickAndMortyApiService,
    private val chatGpt: ChatGptApiService,
    private val userSvc: UserService,
    private val authSvc: AuthService
) {

    private var _isLogged: Boolean = false
    val userData: StateFlow<UserDataResponse?> = userSvc.userData
    val logout: StateFlow<Boolean?> = userSvc.logout

    companion object {
        private const val TAG = "GLOB_DEBUG NETWORK_REPOSITORY"
    }

    init {
        _isLogged = authSvc.firebase.client.auth.currentUser != null
        authSvc.uid.distinctUntilChanged().observeForever { uid ->
            userSvc.fetchUserDocument(uid)
        }
    }

    fun checkAccess(): Boolean {
        return _isLogged
    }

    suspend fun getAllCharacters(): List<CharacterApiModel> {
        val response = rickAndMortySvc.api.getAllCharacters()
        return response.results.map {
            it.asApiModel()
        }
    }

    suspend fun getAllLocations(): List<LocationApiModel> {
        val response = rickAndMortySvc.api.getAllLocations()
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