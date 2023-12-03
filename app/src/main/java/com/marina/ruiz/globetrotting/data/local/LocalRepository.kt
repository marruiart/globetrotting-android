package com.marina.ruiz.globetrotting.data.local

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class LocalRepository(
    private val travelerDao: TravelerDao
) {

    val travelers: Flow<List<TravelerEntity>> = travelerDao.getAllTraveler()

    @WorkerThread
    suspend fun insert(listTravelerEntity: List<TravelerEntity>) =
        travelerDao.createTraveler(listTravelerEntity)

    @WorkerThread
    suspend fun insert(travelerEntity: TravelerEntity) = travelerDao.createTraveler(travelerEntity)
}