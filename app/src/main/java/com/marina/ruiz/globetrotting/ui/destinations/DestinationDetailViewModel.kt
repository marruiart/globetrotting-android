package com.marina.ruiz.globetrotting.ui.destinations

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.local.DestinationEntity
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DestinationDetailViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    private val _destination: MutableStateFlow<Destination> = MutableStateFlow(Destination())
    val destination: StateFlow<Destination>
        get() = _destination.asStateFlow()

    fun updateDescription(destination: Destination, activityScope: CoroutineScope) {
        activityScope.launch {
            val description = repository.fetchDescription(destination.name)
            val shortDescription = repository.fetchShortDescription(destination.name)
            val updatedDestination = DestinationEntity(
                destination.id,
                destination.name,
                destination.type,
                destination.dimension,
                destination.price,
                shortDescription,
                description,
                destination.fav,
                destination.imageRef
            )
            repository.updateDestination(updatedDestination).collect {
                _destination.value = it
            }
        }
    }
}