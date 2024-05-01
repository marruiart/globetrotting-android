package com.marina.ruiz.globetrotting.ui.main.destinations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.BookingPayload
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.User
import com.marina.ruiz.globetrotting.domain.BookNowUseCase
import com.marina.ruiz.globetrotting.ui.main.destinations.adapter.DestinationsListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DestinationsViewModel @Inject constructor(
    private val repository: GlobetrottingRepository,
    private val bookNowUseCase: BookNowUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "GLOB_DEBUG DESTINATIONS_VM"
    }

    lateinit var user: User

    private val _destinations: MutableStateFlow<List<Destination>> = MutableStateFlow(listOf())
    val destinations: StateFlow<List<Destination>>
        get() = _destinations.asStateFlow()

    init {
/*        viewModelScope.launch {
            repository.collectDestinationsList()
        }*/
    }

    fun bindView(adapter: DestinationsListAdapter) {
        viewModelScope.launch {
            repository.destinations.collect { destinationsList ->
                _destinations.value = destinationsList
                adapter.submitList(destinationsList)
            }
        }

        viewModelScope.launch {
            repository.localUser.collect { localUser ->
                localUser?.let {
                    user = it
                }
            }
        }
    }

    fun makeBooking(booking: BookingPayload) {
        viewModelScope.launch {
            bookNowUseCase(booking)
        }
    }

    /*private val _destination: MutableStateFlow<Destination> = MutableStateFlow(Destination())
val destination: StateFlow<Destination>
    get() = _destination.asStateFlow()

fun updateDescription(destination: Destination, activityScope: CoroutineScope) {
    activityScope.launch {
        val description = repository.fetchDescription(destination.name)
        val shortDescription = repository.fetchShortDescription(destination.name)
        repository.updateDestination(updatedDestination).collect {
            _destination.value = it
        }
    }
}*/
}