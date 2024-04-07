package com.marina.ruiz.globetrotting.data.local

import android.util.Log
import androidx.annotation.WorkerThread
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val travelerDao: TravelerDao,
    private val destinationDao: DestinationDao,
    private val bookingDao: BookingDao,
    private val userDao: UserDao
) {

    companion object {
        private const val TAG = "GLOB_DEBUG LOCAL_REPO"
    }

    // TRAVELER
    var travelers: Flow<List<TravelerEntity>> = travelerDao.getAllTravelers()

    @WorkerThread
    suspend fun insertTravelers(listTravelerEntity: List<TravelerEntity>) =
        travelerDao.createTravelers(listTravelerEntity)

    @WorkerThread
    suspend fun insertTraveler(travelerEntity: TravelerEntity) =
        travelerDao.createTraveler(travelerEntity)

    @WorkerThread
    fun getTraveler(id: Int): Flow<TravelerEntity> =
        travelerDao.getTraveler(id)

    @WorkerThread
    suspend fun updateTraveler(traveler: TravelerEntity): Flow<Traveler> {
        travelerDao.updateTraveler(traveler)
        travelers = travelerDao.getAllTravelers()
        return flowOf(traveler.asTraveler())
    }

    // DESTINATION
    var destinations: Flow<List<DestinationEntity>> = destinationDao.getAllDestinations()

    @WorkerThread
    suspend fun insertDestinations(listDestinationEntity: List<DestinationEntity>) =
        destinationDao.createDestinations(listDestinationEntity)

    @WorkerThread
    suspend fun insertDestination(destinationEntity: DestinationEntity) =
        destinationDao.createDestination(destinationEntity)

    @WorkerThread
    suspend fun updateDestination(destination: DestinationEntity): Flow<Destination> {
        destinationDao.updateDestination(destination)
        destinations = destinationDao.getAllDestinations()
        return flowOf(destination.asDestination())
    }

    @WorkerThread
    suspend fun deleteDestination(destination: DestinationEntity) {
        destinationDao.deleteDestination(destination)
        destinations = destinationDao.getAllDestinations()
    }


    // BOOKING
    var bookings: Flow<List<BookingEntity>> = bookingDao.getAllBookings()
    val bookingWithTravelersAndDestinations: Flow<List<FullBooking>> =
        bookingDao.getAllBookingsWithTravelerAndDestination()

    @WorkerThread
    suspend fun insertBooking(bookingEntity: BookingEntity) {
        bookingDao.createBooking(bookingEntity)
        bookings = bookingDao.getAllBookings()
    }

    @WorkerThread
    suspend fun updateBooking(booking: BookingEntity) {
        bookingDao.updateBooking(booking)
        bookings = bookingDao.getAllBookings()
    }

    @WorkerThread
    suspend fun deleteBooking(booking: BookingEntity) {
        bookingDao.deleteBooking(booking)
        bookings = bookingDao.getAllBookings()
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