package com.marina.ruiz.globetrotting.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.ui.main.home.adapter.PopularDestinationsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    companion object {
        private const val TAG = "GLOB_DEBUG HOME_VM"
    }

    private val _destinations: MutableStateFlow<List<Destination>> = MutableStateFlow(listOf())
    val destinations: StateFlow<List<Destination>>
        get() = _destinations.asStateFlow()

    fun bindView(adapter: PopularDestinationsAdapter) {
        viewModelScope.launch {
            repository.destinations.collect { destinationsList ->
                _destinations.value = destinationsList
                adapter.submitList(destinationsList)
            }
        }
    }
}