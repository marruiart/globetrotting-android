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
import com.marina.ruiz.globetrotting.data.local.destination.DestinationFavoritesEntity
import com.marina.ruiz.globetrotting.data.local.favorite.FavoriteDao
import com.marina.ruiz.globetrotting.data.local.favorite.FavoriteEntity
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
    private val userDao: UserDao,
    private val favoriteDao: FavoriteDao
) {

    companion object {
        private const val TAG = "GLOB_DEBUG LOCAL_REPO"
    }

    // DESTINATION
    var destinations: Flow<List<DestinationFavoritesEntity>> = destinationDao.getAllDestinationsWithFavorites()
    var favDestinations: Flow<List<DestinationFavoritesEntity>> = destinationDao.getAllFavDestinations()


    fun updateDestinations(searchQuery: String = "") {
        destinations = destinationDao.getAllDestinationsWithFavorites(searchQuery)
        favDestinations = destinationDao.getAllFavDestinations(searchQuery)
    }


    @WorkerThread
    suspend fun insertDestinations(listDestinationEntity: List<DestinationEntity>) {
        Log.d(TAG, "Inserting destinations...")
        destinationDao.createDestinations(listDestinationEntity)
        destinations = destinationDao.getAllDestinationsWithFavorites()
        Log.d(TAG, "Destinations inserted")
    }

    @WorkerThread
    suspend fun deleteDestinations() {
        destinationDao.deleteDestinations()
    }

    // BOOKING
    var bookings: Flow<List<BookingClientAgentDestinationEntity>> = bookingDao.getAllBookings()

    @WorkerThread
    suspend fun insertBookings(listBookingEntity: List<BookingEntity>) {
        Log.d(TAG, "Inserting bookings...")
        bookingDao.deleteBookings()
        bookingDao.createBookings(listBookingEntity)
        bookings = bookingDao.getAllBookings()
        Log.d(TAG, "Bookings inserted")
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
        Log.d(TAG, "Inserting user...")
        userDao.createUser(userEntity)
        localUser = userDao.getUser()
        Log.d(TAG, "User inserted")
    }

    @WorkerThread
    suspend fun deleteUser() {
        userDao.deleteUser()
        localUser = flowOf(null)
    }

    // FAVORITE
    var localFavorites: Flow<List<FavoriteEntity>?> = favoriteDao.getAllClientFavorites()

    suspend fun insertFavorites(favorites: List<FavoriteEntity>) {
        Log.d(TAG, "Inserting favorites...")
        favoriteDao.createFavorites(favorites)
        localFavorites = favoriteDao.getAllClientFavorites()
        Log.d(TAG, "Favorites inserted")
    }

    @WorkerThread
    suspend fun deleteFavorite(destinationId: String) {
        favoriteDao.deleteFavorite(destinationId)
        Log.d(TAG, "Favorite deleted with destination id: $destinationId")
    }

    @WorkerThread
    suspend fun deleteFavorites() {
        favoriteDao.deleteFavorites()
        localFavorites = flowOf(emptyList())
    }
}