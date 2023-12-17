package com.marina.ruiz.globetrotting.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.local.TravelerEntity
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelerDetailViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    private val _traveler: MutableStateFlow<Traveler> = MutableStateFlow(Traveler())
    val traveler: StateFlow<Traveler>
        get() = _traveler.asStateFlow()

    fun updateDescription(traveler: Traveler, description: String) {
        val updatedTraveler = TravelerEntity(
            traveler.id,
            traveler.name,
            traveler.status ?: "",
            traveler.species ?: "",
            traveler.type ?: "",
            traveler.gender ?: "",
            traveler.image,
            description
        )
        viewModelScope.launch {
            repository.updateTraveler(updatedTraveler).collect {
                _traveler.value = it
            }
        }
    }
}