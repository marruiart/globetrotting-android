package com.marina.ruiz.globetrotting.ui.viewmodels

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DestinationsListViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    private val _destinations: MutableStateFlow<List<Destination>> = MutableStateFlow(listOf())
    val destinations: StateFlow<List<Destination>>
        get() = _destinations.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                // await for refreshDestinationsList()
                repository.refreshDestinationsList()
            } catch (e: IOException) {
                Log.ERROR
            } catch (e: NetworkErrorException) {
                Log.ERROR
            }
            repository.destinations.collect { destinations ->
                _destinations.value = destinations
            }
        }
    }
}