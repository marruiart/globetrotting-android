package com.marina.ruiz.globetrotting.data.local

import android.util.Log
import androidx.annotation.WorkerThread
import com.marina.ruiz.globetrotting.data.local.agent.AgentDao
import com.marina.ruiz.globetrotting.data.local.agent.AgentEntity
import com.marina.ruiz.globetrotting.data.local.booking.BookingClientAgentDestinationEntity
import com.marina.ruiz.globetrotting.data.local.booking.BookingDao
import com.marina.ruiz.globetrotting.data.local.booking.BookingEntity
import com.marina.ruiz.globetrotting.data.local.destination.DestinationDao
import com.marina.ruiz.globetrotting.data.local.destination.DestinationEntity
import com.marina.ruiz.globetrotting.data.local.user.UserDao
import com.marina.ruiz.globetrotting.data.local.user.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val destinationDao: DestinationDao,
    private val agentDao: AgentDao,
    private val bookingDao: BookingDao,
    private val userDao: UserDao
) {

    companion object {
        private const val TAG = "GLOB_DEBUG LOCAL_REPO"
    }

    // DESTINATION
    var destinations: Flow<List<DestinationEntity>> = destinationDao.getAllDestinations()

    @WorkerThread
    suspend fun insertDestinations(listDestinationEntity: List<DestinationEntity>) {
        destinationDao.createDestinations(listDestinationEntity)
        destinations = destinationDao.getAllDestinations()
        Log.d(TAG, "Insert destinations")
    }

    @WorkerThread
    suspend fun deleteDestinations() {
        destinationDao.deleteDestinations()
    }

    // BOOKING
    var bookings: Flow<List<BookingClientAgentDestinationEntity>> = bookingDao.getAllBookings()

    @WorkerThread
    suspend fun insertBookings(listBookingEntity: List<BookingEntity>) {
        Log.d(TAG, "Insert bookings")
        bookingDao.deleteBookings()
        bookingDao.createBookings(listBookingEntity)
        bookings = bookingDao.getAllBookings()
    }

    @WorkerThread
    suspend fun deleteBookings() {
        bookingDao.deleteBookings()
    }

    // AGENTS
    var agents: Flow<List<AgentEntity>> = agentDao.getAllAgents()

    @WorkerThread
    suspend fun insertAgents(listAgentsEntity: List<AgentEntity>) {
        agentDao.createAgents(listAgentsEntity)
        agents = agentDao.getAllAgents()
    }

    @WorkerThread
    suspend fun deleteAgents() {
        agentDao.deleteAgents()
    }

    // USER
    var localUser: Flow<UserEntity?> = userDao.getUser()

    @WorkerThread
    suspend fun insertUser(userEntity: UserEntity) {
        userDao.createUser(userEntity)
        localUser = userDao.getUser()
        Log.d(TAG, "Insert user")
    }

    @WorkerThread
    suspend fun deleteUser() {
        userDao.deleteUser()
        localUser = flowOf(null)
    }
}