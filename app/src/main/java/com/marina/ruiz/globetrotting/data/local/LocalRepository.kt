package com.marina.ruiz.globetrotting.data.local

import android.util.Log
import androidx.annotation.WorkerThread
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val destinationDao: DestinationDao,
    //private val bookingDao: BookingDao,
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

    // BOOKING
/*    var bookings: Flow<List<BookingEntity>> = bookingDao.getAllBookings()
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
    }*/

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