package com.marina.ruiz.globetrotting.data.network

import androidx.lifecycle.distinctUntilChanged
import com.marina.ruiz.globetrotting.data.network.chatGpt.ChatGptApiService
import com.marina.ruiz.globetrotting.data.network.chatGpt.model.ChatGptResponse
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.BookingsService
import com.marina.ruiz.globetrotting.data.network.firebase.DestinationsService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.DestinationPayload
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.AgentResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.UserDataResponse
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
    suspend fun updateDestination(destination: DestinationPayload) =
        destinationsSvc.updateDestination(destination)

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


    suspend fun getShortDescription(destination: String, country: String): String {
        val requestBody =
            createRequestBody("Como si fuera una agencia de viajes, crea una descripción muy corta (10 palabras máximo) sobre el destino de viaje '${destination}' en $country")
        val response: ChatGptResponse = chatGpt.api.getResponse(requestBody)
        try {
            return response.asApiModel().text.trim().replace("\"", "")
        } catch (ex: Exception) {
            throw ex
        }
    }


    suspend fun getLongDescription(destination: String, country: String): String {
        val requestBody =
            createRequestBody("Como si fuera una agencia de viajes, crea una descripción (100 palabras en 2 o 3 párrafos) sobre el destino de viaje '$destination' en $country")
        try {
            val response: ChatGptResponse = chatGpt.api.getResponse(requestBody)
            return response.asApiModel().text.trim()
        } catch (ex: Exception) {
            throw ex
        }
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