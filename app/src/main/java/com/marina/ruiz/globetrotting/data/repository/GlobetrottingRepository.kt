package com.marina.ruiz.globetrotting.data.repository

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.annotation.WorkerThread
import com.marina.ruiz.globetrotting.data.local.LocalRepository
import com.marina.ruiz.globetrotting.data.local.agent.AgentEntity
import com.marina.ruiz.globetrotting.data.local.booking.BookingEntity
import com.marina.ruiz.globetrotting.data.local.booking.asBookingList
import com.marina.ruiz.globetrotting.data.local.destination.DestinationEntity
import com.marina.ruiz.globetrotting.data.local.destination.asDestinationList
import com.marina.ruiz.globetrotting.data.local.user.UserEntity
import com.marina.ruiz.globetrotting.data.network.NetworkRepository
import com.marina.ruiz.globetrotting.data.network.firebase.model.asAgentsEntityList
import com.marina.ruiz.globetrotting.data.network.firebase.model.asBookingEntityList
import com.marina.ruiz.globetrotting.data.network.firebase.model.asDestinationsEntityList
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobetrottingRepository @Inject constructor(
    private val localRepository: LocalRepository, private val networkRepository: NetworkRepository
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
            return localRepository.destinations.map { destinations ->
                destinations.asDestinationList()
            }
        }

    val bookings: Flow<List<Booking>>
        get() {
            return localRepository.bookings.map {
                it.asBookingList()
            }
        }

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

    suspend fun collectData(): Unit = withContext(Dispatchers.IO) {
        val agentsResponse = networkRepository.agentsResponse
        val destinationsResponse = networkRepository.destinationsResponse
        val bookingsResponse = networkRepository.bookingsResponse
        combine(
            agentsResponse, destinationsResponse, bookingsResponse
        ) { agents, destinations, bookings ->
            Log.w(TAG, "Collected agents network repo: $agents")
            createAgents(agents.asAgentsEntityList())
            Log.w(TAG, "Collected destinations network repo: $destinations")
            createDestinations(destinations.asDestinationsEntityList())
            Triple(agents, destinations, bookings)
        }.filter { (agents, destinations, _) ->
            agents.isNotEmpty() && destinations.isNotEmpty()
        }.collect { (_, _, bookings) ->
            Log.w(TAG, "Collected bookings network repo: $bookings")
            createBookings(bookings.asBookingEntityList())
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

    private suspend fun createAgents(agents: List<AgentEntity>) =
        localRepository.insertAgents(agents)

    private suspend fun createBookings(bookings: List<BookingEntity>) =
        localRepository.insertBookings(bookings)

    private suspend fun createDestinations(destinations: List<DestinationEntity>) =
        localRepository.insertDestinations(destinations)

    suspend fun deleteUser() = localRepository.deleteUser()

    /*    suspend fun updateBooking(booking: BookingEntity) {
            localRepository.updateBooking(booking)
        }

        suspend fun deleteBooking(booking: BookingEntity) {
            localRepository.deleteBooking(booking)
        }
*/

}