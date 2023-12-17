package com.marina.ruiz.globetrotting.data.local

import androidx.annotation.WorkerThread
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val travelerDao: TravelerDao,
    private val destinationDao: DestinationDao,
    private val bookingDao: BookingDao
) {
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
    suspend fun updateDestination(destination: DestinationEntity) {
        destinationDao.updateDestination(destination)
        destinations = destinationDao.getAllDestinations()
    }

    // BOOKING
    val bookings: Flow<List<BookingEntity>> = bookingDao.getAllBookings()
    val bookingWithTravelersAndDestinations: Flow<List<FullBooking>> =
        bookingDao.getAllBookingsWithTravelerAndDestination()

    @WorkerThread
    suspend fun insertBooking(bookingEntity: BookingEntity) =
        bookingDao.createBooking(bookingEntity)
}