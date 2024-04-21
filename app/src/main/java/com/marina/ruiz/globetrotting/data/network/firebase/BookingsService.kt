package com.marina.ruiz.globetrotting.data.network.firebase

import android.util.Log
import com.marina.ruiz.globetrotting.data.network.firebase.model.BookingResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.DocumentData
import com.marina.ruiz.globetrotting.data.network.firebase.model.asBookingResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingsService @Inject constructor(private val firebase: FirebaseService) {

    private val _bookingData = MutableStateFlow<List<BookingResponse>>(emptyList())
    val bookingsResponse: StateFlow<List<BookingResponse>>
        get() = _bookingData

    companion object {
        private const val TAG = "GLOB_DEBUG BOOKINGS_SERVICE"
        private const val BOOKINGS_COLLECTION = "bookings"
    }

    fun fetchBookings() {
        Log.i(TAG, "Fetch bookings")
        val uid = firebase.client.auth.uid
        firebase.getCollectionRef(BOOKINGS_COLLECTION)
            .whereEqualTo("client_id", uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                _bookingData.value = snapshot!!.map { doc ->
                    (doc.data as DocumentData).asBookingResponse()
                }
                Log.d(TAG, "Current bookings: ${_bookingData.value}")
            }
    }

}