package com.marina.ruiz.globetrotting.data.local.destination

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DestinationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createDestinations(listDestinationEntity: List<DestinationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createDestination(destinationEntity: DestinationEntity)

    @Query("SELECT * FROM destination")
    fun getAllDestinations(): Flow<List<DestinationEntity>>

    @Update
    suspend fun updateDestination(destinationEntity: DestinationEntity)

    @Query("DELETE FROM destination")
    suspend fun deleteDestinations()
}