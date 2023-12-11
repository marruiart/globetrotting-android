package com.marina.ruiz.globetrotting.data.local

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val travelerDao: TravelerDao,
    private val destinationDao: DestinationDao
) {
    val travelers: Flow<List<TravelerEntity>> = travelerDao.getAllTravelers()
    val destinations: Flow<List<DestinationEntity>> = destinationDao.getAllDestinations()

    // TRAVELER
    @WorkerThread
    suspend fun insertTravelers(listTravelerEntity: List<TravelerEntity>) =
        travelerDao.createTravelers(listTravelerEntity)

    @WorkerThread
    suspend fun insertTraveler(travelerEntity: TravelerEntity) =
        travelerDao.createTraveler(travelerEntity)

    // DESTINATION
    @WorkerThread
    suspend fun insertDestinations(listDestinationEntity: List<DestinationEntity>) =
        destinationDao.createDestinations(listDestinationEntity)

    @WorkerThread
    suspend fun insertDestination(destinationEntity: DestinationEntity) =
        destinationDao.createDestination(destinationEntity)
}