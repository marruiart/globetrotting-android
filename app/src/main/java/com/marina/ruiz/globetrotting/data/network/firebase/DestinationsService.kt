package com.marina.ruiz.globetrotting.data.network.firebase

import android.util.Log
import com.google.firebase.firestore.QuerySnapshot
import com.marina.ruiz.globetrotting.data.network.firebase.model.DocumentData
import com.marina.ruiz.globetrotting.data.network.firebase.model.asDestinationResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.DestinationPayload
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.DestinationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DestinationsService @Inject constructor(private val firebase: FirebaseService) {

    private val _destinationsResponse = MutableStateFlow<List<DestinationResponse>>(emptyList())
    val destinationsResponse: StateFlow<List<DestinationResponse>>
        get() = _destinationsResponse

    companion object {
        private const val TAG = "GLOB_DEBUG DESTINATIONS_SERVICE"
        private const val DESTINATIONS_COLLECTION = "destinations"
    }

    suspend fun updateDestination(destination: DestinationPayload) {
        firebase.updateDocument(DESTINATIONS_COLLECTION, destination.toMap(), destination.id)
    }

    fun fetchDestinations() {
        Log.i(TAG, "Fetch destinations")
        firebase.getCollectionRef(DESTINATIONS_COLLECTION)
            .addSnapshotListener { snapshot: QuerySnapshot?, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                } else if (snapshot != null) {
                    _destinationsResponse.value = snapshot.map { doc ->
                        (doc.data as DocumentData).asDestinationResponse()
                    }
                    Log.d(TAG, "Current destinations: ${_destinationsResponse.value.map{it.name}}")
                }
            }
    }

}