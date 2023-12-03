package com.marina.ruiz.globetrotting.data.local

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class GlobetrottingLocalRepository (private val pokemonDao: TravelerDao) {

    val allTraveler: Flow<List<TravelerEntity>> = pokemonDao.getAllTraveler()

    @WorkerThread
    suspend fun insert(listTravelerEntity: List<TravelerEntity>) =
        pokemonDao.createTraveler(listTravelerEntity)

    @WorkerThread
    suspend fun insert(pokemonEntity: TravelerEntity) = pokemonDao.createTraveler(pokemonEntity)
}