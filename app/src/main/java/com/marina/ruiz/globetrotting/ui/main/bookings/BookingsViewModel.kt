package com.marina.ruiz.globetrotting.ui.main.bookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.ui.main.bookings.adapter.BookingsListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    companion object {
        private const val TAG = "GLOB_DEBUG BOOKINGS_VM"
    }

    private val _bookings: MutableStateFlow<List<Booking>> = MutableStateFlow(listOf())
    val bookings: StateFlow<List<Booking>>
        get() = _bookings.asStateFlow()

    private val _showNoBookingDialog: MutableLiveData<Boolean> = MutableLiveData(false)
    val showNoBookingDialog: LiveData<Boolean>
        get() = _showNoBookingDialog

    fun bindView(adapter: BookingsListAdapter) {
        viewModelScope.launch {
            repository.bookings.collect { bookingsList ->
                _bookings.value = bookingsList
                _showNoBookingDialog.value = bookingsList.isEmpty()
                adapter.submitList(bookingsList)
            }
        }
    }
}