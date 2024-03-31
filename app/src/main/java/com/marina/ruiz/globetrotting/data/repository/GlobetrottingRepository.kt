package com.marina.ruiz.globetrotting.data.repository

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.marina.ruiz.globetrotting.data.local.BookingEntity
import com.marina.ruiz.globetrotting.data.local.DestinationEntity
import com.marina.ruiz.globetrotting.data.local.LocalRepository
import com.marina.ruiz.globetrotting.data.local.TravelerEntity
import com.marina.ruiz.globetrotting.data.local.UserEntity
import com.marina.ruiz.globetrotting.data.local.asDestinationList
import com.marina.ruiz.globetrotting.data.local.asFullBookingList
import com.marina.ruiz.globetrotting.data.local.asTravelerList
import com.marina.ruiz.globetrotting.data.network.NetworkRepository
import com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model.asEntityModelList
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import com.marina.ruiz.globetrotting.data.repository.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobetrottingRepository @Inject constructor(
    private val localRepository: LocalRepository,
    private val networkRepository: NetworkRepository
) {
    var _isLogged: Boolean = false
    val logout: StateFlow<Boolean?> = networkRepository.logout

    companion object {
        private const val TAG = "GLOB_DEBUG GLOBETROTTING_REPOSITORY"
    }

    init {
        _isLogged = networkRepository.checkAccess()
    }

    // Expose data operations
    val travelers: Flow<List<Traveler>>
        get() {
            // offline first
            return localRepository.travelers.map {
                it.asTravelerList()
            }
        }

    val destinations: Flow<List<Destination>>
        get() {
            // offline first
            return localRepository.destinations.map {
                it.asDestinationList()
            }
        }

    val bookings: Flow<List<Booking>>
        get() {
            // offline first
            return localRepository.bookingWithTravelersAndDestinations.map {
                it.asFullBookingList()
            }
        }

    val localUser: Flow<User?>
        get() {
            // offline first
            return localRepository.user.distinctUntilChangedBy { user -> user?.id }.map { user ->
                Log.i(TAG, "User changed in room: ${user?.uid}")
                user?.asUser()
            }
        }

    fun checkAccess(): Boolean {
        return _isLogged
    }

    suspend fun collectUserData(): Unit = withContext(Dispatchers.IO) {
        networkRepository.userData.distinctUntilChangedBy { userData -> userData?.uid }
            .collect { userData ->
                Log.w(TAG, "Collected user network repo: ${userData?.uid}")
                userData?.let { data ->
                    createUser(data.asUserEntity())
                }
            }
    }

    suspend fun refreshTravelersList() = withContext(Dispatchers.IO) {
        // SCOPE: suspendable code -> executed asynchronously in a coroutine.
        // Dispatchers.IO is a special thread for network operations
        val characterApiModelList = networkRepository.getAllCharacters()
        localRepository.insertTravelers(characterApiModelList.asEntityModelList())
    }

    suspend fun refreshDestinationsList() = withContext(Dispatchers.IO) {
        val locationApiModelList = networkRepository.getAllLocations()
        localRepository.insertDestinations(locationApiModelList.asEntityModelList())
    }

    @WorkerThread
    suspend fun fetchDescription(name: String): String {
        var description = ""
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Solicitando descripción...")
                delay(30000)
                val gptDescription = networkRepository.getLongDescription(name)
                Log.d(TAG, gptDescription)
                description = gptDescription
            } catch (e: IOException) {
                Log.ERROR
            } catch (e: NetworkErrorException) {
                Log.ERROR
            }
        }
        return description
    }

    @WorkerThread
    suspend fun fetchShortDescription(name: String): String {
        var description = ""
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Solicitando descripción corta...")
                delay(10000)
                val gptShortDescription = networkRepository.getShortDescription(name)
                Log.d(TAG, gptShortDescription)
                description = gptShortDescription
            } catch (e: IOException) {
                Log.ERROR
            } catch (e: NetworkErrorException) {
                Log.ERROR
            }
        }
        return description
    }

    suspend fun updateTraveler(traveler: TravelerEntity): Flow<Traveler> {
        return localRepository.updateTraveler(traveler)
    }

    suspend fun updateDestination(destination: DestinationEntity): Flow<Destination> {
        return localRepository.updateDestination(destination)
    }

    suspend fun deleteDestination(destination: DestinationEntity) {
        localRepository.deleteDestination(destination)
    }

    suspend fun updateBooking(booking: BookingEntity) {
        localRepository.updateBooking(booking)
    }

    suspend fun deleteBooking(booking: BookingEntity) {
        localRepository.deleteBooking(booking)
    }

    suspend fun createBooking(booking: BookingEntity) = localRepository.insertBooking(booking)

    private suspend fun createUser(user: UserEntity) = localRepository.insertUser(user)

    suspend fun deleteUser() = localRepository.deleteUser()

}