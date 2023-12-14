package com.marina.ruiz.globetrotting.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DestinationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createDestinations(listDestinationEntity: List<DestinationEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createDestination(destinationEntity: DestinationEntity)

    @Query("SELECT * FROM destination")
    fun getAllDestinations(): Flow<List<DestinationEntity>>
}