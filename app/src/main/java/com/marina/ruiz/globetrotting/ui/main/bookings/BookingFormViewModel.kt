package com.marina.ruiz.globetrotting.ui.main.bookings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
//import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BookingFormViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    private val _destinations: MutableStateFlow<List<Destination>> = MutableStateFlow(listOf())
    val destinations: StateFlow<List<Destination>>
        get() = _destinations.asStateFlow()

/*    suspend fun createBooking(booking: Booking) {
        repository.createBooking(booking.asBookingEntity())
        repository.refreshTravelersList()
    }

    suspend fun updateBooking(booking: Booking) {
        repository.updateBooking(booking.asBookingEntity())
        repository.refreshTravelersList()
    }*/

    init {
        viewModelScope.launch {
            try {
                repository.destinations.collect { destinations ->
                    _destinations.value = destinations
                }
            } catch (e: IOException) {
                Log.ERROR
            }
        }
    }
}