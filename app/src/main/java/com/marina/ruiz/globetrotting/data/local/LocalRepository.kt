package com.marina.ruiz.globetrotting.data.local

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val travelerDao: TravelerDao
) {

    val travelers: Flow<List<TravelerEntity>> = travelerDao.getAllTraveler()

    @WorkerThread
    suspend fun insert(listTravelerEntity: List<TravelerEntity>) =
        travelerDao.createTravelers(listTravelerEntity)

    @WorkerThread
    suspend fun insert(travelerEntity: TravelerEntity) = travelerDao.createTravelers(travelerEntity)
}