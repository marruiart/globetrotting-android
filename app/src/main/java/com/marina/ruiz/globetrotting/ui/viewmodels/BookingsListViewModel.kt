package com.marina.ruiz.globetrotting.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BookingsListViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    private val _bookings: MutableStateFlow<List<Booking>> = MutableStateFlow(listOf())
    val bookings: StateFlow<List<Booking>>
        get() = _bookings.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                repository.bookings.collect { bookings ->
                    _bookings.value = bookings
                }
            } catch (e: IOException) {
                Log.ERROR
            }
        }
    }
}