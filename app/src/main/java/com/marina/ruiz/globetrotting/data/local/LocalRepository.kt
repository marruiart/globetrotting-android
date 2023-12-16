package com.marina.ruiz.globetrotting.data.local

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val travelerDao: TravelerDao,
    private val destinationDao: DestinationDao,
    private val bookingDao: BookingDao
) {
    // TRAVELER
    val travelers: Flow<List<TravelerEntity>> = travelerDao.getAllTravelers()

    @WorkerThread
    suspend fun insertTravelers(listTravelerEntity: List<TravelerEntity>) =
        travelerDao.createTravelers(listTravelerEntity)

    @WorkerThread
    suspend fun insertTraveler(travelerEntity: TravelerEntity) =
        travelerDao.createTraveler(travelerEntity)

    // DESTINATION
    val destinations: Flow<List<DestinationEntity>> = destinationDao.getAllDestinations()

    @WorkerThread
    suspend fun insertDestinations(listDestinationEntity: List<DestinationEntity>) =
        destinationDao.createDestinations(listDestinationEntity)

    @WorkerThread
    suspend fun insertDestination(destinationEntity: DestinationEntity) =
        destinationDao.createDestination(destinationEntity)

    // BOOKING
    val bookings: Flow<List<BookingEntity>> = bookingDao.getAllBookings()
    val bookingWithTravelersAndDestinations: Flow<List<FullBooking>> = bookingDao.getAllBookingsWithTravelerAndDestination()

    @WorkerThread
    suspend fun insertBooking(bookingEntity: BookingEntity) =
        bookingDao.createBooking(bookingEntity)
}