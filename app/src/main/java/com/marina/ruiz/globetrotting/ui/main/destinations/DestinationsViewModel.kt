package com.marina.ruiz.globetrotting.ui.main.destinations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.BookingPayload
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.Favorite
import com.marina.ruiz.globetrotting.data.repository.model.User
import com.marina.ruiz.globetrotting.domain.BookNowUseCase
import com.marina.ruiz.globetrotting.domain.ToggleFavoriteUseCase
import com.marina.ruiz.globetrotting.ui.main.destinations.adapter.DestinationsListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DestinationsViewModel @Inject constructor(
    private val repository: GlobetrottingRepository,
    private val bookNowUseCase: BookNowUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "GLOB_DEBUG DESTINATIONS_VM"
    }

    lateinit var user: User
    private var favorites: MutableList<Favorite> = mutableListOf()

    private val _destinations: MutableStateFlow<List<Destination>> = MutableStateFlow(listOf())
    val destinations: StateFlow<List<Destination>>
        get() = _destinations.asStateFlow()

    fun bindView(adapter: DestinationsListAdapter) {
        viewModelScope.launch {
            repository.localUser.collect { localUser ->
                localUser?.let {
                    user = it
                }
            }
        }
        viewModelScope.launch {
            collectDestinationsWithFavorites(adapter)
        }
    }

    private suspend fun collectDestinationsWithFavorites(adapter: DestinationsListAdapter) {
        val favoritesResponse = repository.favorites
        val destinationsResponse = repository.destinations

        combine(favoritesResponse, destinationsResponse) { favs, destinations ->
            Pair(favs, destinations)
        }.collect { (favs, destinations) ->
            favorites = favs.toMutableList()
            _destinations.value = destinations
            adapter.submitList(destinations)
        }
    }

    fun makeBooking(booking: BookingPayload) {
        viewModelScope.launch {
            bookNowUseCase(booking)
        }
    }

    fun handleFavorite(destinationId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            val favorite = Favorite(destinationId, destinationId)
            if (isFavorite) {
                val added = favorites.add(favorite)
                Log.d(TAG, "${favorite.id} added: $added")
            } else {
                val removed = favorites.removeIf { it.destinationId == destinationId }
                Log.d(TAG, "${favorite.id} removed: $removed")
                if (removed) {
                    repository.deleteFavorite(favorite.destinationId)
                }
            }
            toggleFavoriteUseCase(favorites)
            _destinations.value = _destinations.value.map {
                if (it.id == destinationId) it.copy(favorite = isFavorite) else it
            }
        }
    }

    fun fetchDescription(destination: Destination) {
        viewModelScope.launch {
            val description = repository.fetchDescription(destination.name)
            repository.updateDestination(destination.asDestinationPayload(newDescription = description))
            //val shortDescription = repository.fetchShortDescription(destination.name)
        }
    }
}