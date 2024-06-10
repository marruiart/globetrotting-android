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

    @Query(
        "SELECT d.id AS id, d.name AS name, d.type AS type, d.dimension AS dimension, d.price AS price, " +
                "d.shortDescription AS shortDescription, d.description AS description, f.id AS favId " +
                "FROM destination d LEFT JOIN favorite AS f ON f.destinationId = d.id"
    )
    fun getAllDestinationsWithFavorites(): Flow<List<DestinationFavoritesEntity>>

    @Update
    suspend fun updateDestination(destinationEntity: DestinationEntity)

    @Query("DELETE FROM destination")
    suspend fun deleteDestinations()
}