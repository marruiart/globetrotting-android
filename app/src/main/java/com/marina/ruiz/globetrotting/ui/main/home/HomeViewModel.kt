package com.marina.ruiz.globetrotting.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.ui.main.destinations.adapter.DestinationsListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    companion object {
        private const val TAG = "GLOB_DEBUG HOME_VM"
    }

    fun bindView(adapter: DestinationsListAdapter) {
        viewModelScope.launch {
            repository.destinations.collect { popularDestinations ->
                adapter.submitList(popularDestinations)
            }
        }
    }
}