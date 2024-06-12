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
import kotlinx.coroutines.Job
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
    private var collectJob: Job? = null
    private var _favorites: MutableList<Favorite> = mutableListOf()
    var onlyFavorites: Boolean = false

    /**
     * Binds the DestinationsListAdapter to the ViewModel and updates the destinations list based on the search query.
     *
     * @param adapter The DestinationsListAdapter to bind
     * @param searchQuery The search query to filter the destinations list (default is an empty string)
     */
    fun bindView(adapter: DestinationsListAdapter, searchQuery: String = "") {
        repository.updateDestinations(searchQuery)
        repository.collectLocalUser { localUser ->
            localUser?.let {
                user = it
            }
        }
        collectDestinationsWithFavorites(adapter)
    }

    /**
     * Collects the destinations and favorite destinations and submits the list to the adapter.
     *
     * @param adapter The DestinationsListAdapter to submit the list to
     */
    private fun collectDestinationsWithFavorites(adapter: DestinationsListAdapter) {
        stopCollecting(collectJob)

        collectJob = viewModelScope.launch {
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

    /**
     * Stops collecting data if there is an ongoing job.
     *
     * @param job The job to be stopped
     */
    private fun stopCollecting(job: Job?) {
        job?.cancel()
    }

    /**
     * Makes a booking using the provided BookingPayload.
     *
     * @param booking The BookingPayload containing the booking details
     */
    fun makeBooking(booking: BookingPayload) {
        viewModelScope.launch {
            bookNowUseCase(booking)
        }
    }

    /**
     * Handles adding or removing a favorite destination.
     *
     * @param destinationId The ID of the destination to be added or removed from favorites
     * @param isFavorite Whether the destination is currently marked as a favorite
     */
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

    /**
     * Fetches the description for a given destination and updates the repository with the new information.
     *
     * @param destination The destination to fetch the description for
     * @param onResponse The callback to execute with the fetched description
     */
    fun fetchDescription(destination: Destination, onResponse: (description: String) -> Unit) {
        viewModelScope.launch {
            val description = repository.fetchDescription(destination.name, destination.country)
            onResponse(description)
            val shortDescription =
                repository.fetchShortDescription(destination.name, destination.country)
            repository.updateDestination(
                destination.asDestinationPayload(description, shortDescription)
            )
        }
    }
}