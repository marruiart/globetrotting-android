package com.marina.ruiz.globetrotting.data.repository

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.annotation.WorkerThread
import com.marina.ruiz.globetrotting.data.local.DestinationEntity
import com.marina.ruiz.globetrotting.data.local.LocalRepository
import com.marina.ruiz.globetrotting.data.local.UserEntity
import com.marina.ruiz.globetrotting.data.local.asDestinationList
import com.marina.ruiz.globetrotting.data.network.NetworkRepository
import com.marina.ruiz.globetrotting.data.network.firebase.model.asDestinationEntityList
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
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

    companion object {
        private const val TAG = "GLOB_DEBUG GLOBETROTTING_REPOSITORY"
    }

    // OFFLINE FIRST DATA OPERATIONS

    val localUser: Flow<User?>
        get() {
            return localRepository.localUser.map { user ->
                Log.i(TAG, "User changed in room: ${user?.uid}")
                user?.asUser()
            }
        }

    val destinations: Flow<List<Destination>>
        get() {
            return localRepository.destinations.map {destinations ->
                destinations.asDestinationList()
            }
        }

    /*    val bookings: Flow<List<Booking>>
            get() {
                return localRepository.bookingWithTravelersAndDestinations.map {
                    it.asFullBookingList()
                }
            }*/


    // FETCH FROM NETWORK REPOSITORY

    val logout: StateFlow<Boolean?> = networkRepository.logout

    fun checkAccess(): Boolean {
        return networkRepository.checkAccess()
    }

    suspend fun collectUserData(): Unit = withContext(Dispatchers.IO) {
        networkRepository.userResponse.collect { userData ->
            Log.w(TAG, "Collected user network repo: ${userData?.uid}")
            userData?.let { data ->
                createUser(data.asUserEntity())
            }
        }
    }

    suspend fun collectDestinationsList(): Unit = withContext(Dispatchers.IO) {
        networkRepository.destinationsResponse.collect { destinationsResponse ->
            val destinationsListEntity = destinationsResponse.asDestinationEntityList()
            localRepository.insertDestinations(destinationsListEntity)
        }
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


    // LOCAL DATABASE CRUD FUNCTIONS

    private suspend fun createUser(user: UserEntity) = localRepository.insertUser(user)

    suspend fun deleteUser() = localRepository.deleteUser()

    /*    suspend fun updateBooking(booking: BookingEntity) {
            localRepository.updateBooking(booking)
        }

        suspend fun deleteBooking(booking: BookingEntity) {
            localRepository.deleteBooking(booking)
        }

        suspend fun createBooking(booking: BookingEntity) = localRepository.insertBooking(booking)*/

}