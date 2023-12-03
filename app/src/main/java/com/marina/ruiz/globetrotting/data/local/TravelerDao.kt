package com.marina.ruiz.globetrotting.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTraveler(listTravelerEntity: List<TravelerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTraveler(pokemonEntity: TravelerEntity)

    @Query("SELECT * FROM traveler")
    fun getAllTraveler(): Flow<List<TravelerEntity>>
}