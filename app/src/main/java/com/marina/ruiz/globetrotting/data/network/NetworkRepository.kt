package com.marina.ruiz.globetrotting.data.network

import androidx.lifecycle.distinctUntilChanged
import com.marina.ruiz.globetrotting.data.network.chatGpt.ChatGptApiService
import com.marina.ruiz.globetrotting.data.network.chatGpt.model.ChatGptResponse
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.BookingsService
import com.marina.ruiz.globetrotting.data.network.firebase.DestinationsService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.AgentResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.UserDataResponse
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val chatGpt: ChatGptApiService,
    private val userSvc: UserService,
    private val destinationsSvc: DestinationsService,
    private val bookingsSvc: BookingsService,
    private val authSvc: AuthService
) {

    companion object {
        private const val TAG = "GLOB_DEBUG NETWORK_REPOSITORY"
    }

    // USER SERVICE
    val userResponse: StateFlow<UserDataResponse?> = userSvc.userResponse
    val agentsResponse: StateFlow<List<AgentResponse>> = userSvc.agentsResponse
    val logout: StateFlow<Boolean?> = userSvc.logout

    // DESTINATIONS SERVICE
    val destinationsResponse = destinationsSvc.destinationsResponse

    // BOOKINGS SERVICE
    val bookingsResponse = bookingsSvc.bookingsResponse

    init {
        fetchData()
    }

    private fun fetchData() {
        authSvc.uid.distinctUntilChanged().observeForever { uid ->
            userSvc.fetchUserDocument(uid)
            bookingsSvc.fetchBookings()
        }
        userSvc.fetchAgents()
        destinationsSvc.fetchDestinations()
    }

    fun checkAccess(): Boolean = authSvc.firebase.client.auth.currentUser != null

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