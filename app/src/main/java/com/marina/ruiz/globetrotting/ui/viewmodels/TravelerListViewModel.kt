package com.marina.ruiz.globetrotting.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class TravelerListViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    private val _travelers: MutableStateFlow<List<Traveler>> = MutableStateFlow(listOf())
    val travelers: StateFlow<List<Traveler>>
        get() = _travelers.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                repository.refreshList()
            } catch (e: IOException) {
                Log.ERROR
            }
            viewModelScope.launch {
                repository.travelers.collect { // subscription
                    _travelers.value = it
                }
            }
        }
    }
}