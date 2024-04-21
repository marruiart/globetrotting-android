package com.marina.ruiz.globetrotting.ui.main.destinations

import androidx.lifecycle.ViewModel
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DestinationDetailViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

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