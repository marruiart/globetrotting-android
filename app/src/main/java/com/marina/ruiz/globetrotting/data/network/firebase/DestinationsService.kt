package com.marina.ruiz.globetrotting.data.network.firebase

import android.util.Log
import com.marina.ruiz.globetrotting.data.network.firebase.model.DocumentData
import com.marina.ruiz.globetrotting.data.network.firebase.model.FirebaseDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DestinationsService @Inject constructor(private val firebase: FirebaseService) {

    private val _destinationsResponse = MutableStateFlow<List<FirebaseDocument>>(emptyList())
    val destinationsResponse: StateFlow<List<FirebaseDocument>>
        get() = _destinationsResponse

    companion object {
        private const val TAG = "GLOB_DEBUG DESTINATIONS_SERVICE"
        private const val DESTINATIONS_COLLECTION = "destinations"
    }

    fun fetchDestinations() {
        Log.i(TAG, "Fetch destinations")
        firebase.getCollection(DESTINATIONS_COLLECTION)?.addOnSuccessListener { destinations ->
            val docs: List<FirebaseDocument> =
                destinations.documents.map { doc -> FirebaseDocument(doc.id, doc.data as DocumentData) }
            _destinationsResponse.value = docs
            Log.d(TAG, docs.toString())
        }
    }

}