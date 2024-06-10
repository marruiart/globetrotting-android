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
import kotlinx.coroutines.flow.Flow
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
    private var _favorites: MutableList<Favorite> = mutableListOf()

    fun bindView(adapter: DestinationsListAdapter, onlyFavorites: Boolean = false) {
        viewModelScope.launch {
            repository.localUser.collect { localUser ->
                localUser?.let {
                    user = it
                }
            }
        }
        collectDestinationsWithFavorites(adapter, onlyFavorites)
    }

    private fun collectDestinationsWithFavorites(
        adapter: DestinationsListAdapter,
        onlyFavorites: Boolean
    ) {
        viewModelScope.launch {
            val favoritesResponse = repository.favorites
            val destinationsResponse: Flow<List<Destination>> = if (onlyFavorites) {
                repository.favDestinations
            } else {
                repository.destinations
            }

            combine(favoritesResponse, destinationsResponse) { favorites, destinations ->
                Pair(favorites, destinations)
            }.collect { (favorites, destinations) ->
                _favorites = favorites.toMutableList()
                adapter.submitList(destinations)
            }
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
                val added = _favorites.add(favorite)
                Log.d(TAG, "${favorite.id} added: $added")
            } else {
                val removed = _favorites.removeIf { it.destinationId == destinationId }
                Log.d(TAG, "${favorite.id} removed: $removed")
                if (removed) {
                    repository.deleteFavorite(favorite.destinationId)
                }
            }
            toggleFavoriteUseCase(_favorites)
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