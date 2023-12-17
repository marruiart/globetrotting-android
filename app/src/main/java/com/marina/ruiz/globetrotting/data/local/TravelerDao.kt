package com.marina.ruiz.globetrotting.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTravelers(listTravelerEntity: List<TravelerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTraveler(travelerEntity: TravelerEntity)

    @Query("SELECT * FROM traveler")
    fun getAllTravelers(): Flow<List<TravelerEntity>>

    @Query("SELECT * FROM traveler WHERE id = :id")
    fun getTraveler(id: Int): Flow<TravelerEntity>

    @Update
    suspend fun updateTraveler(travelerEntity: TravelerEntity)
}