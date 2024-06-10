package com.marina.ruiz.globetrotting.data.local.favorite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createFavorites(favoritesList: List<FavoriteEntity>)

    @Query(
        "SELECT f.id AS id, f.destinationId AS destinationId " +
                "FROM favorite AS f " +
                "INNER JOIN destination AS d ON d.id = f.destinationId"
    )
    fun getAllClientFavorites(): Flow<List<FavoriteEntity>>

    @Query("DELETE FROM favorite")
    suspend fun deleteFavorites()

    @Query("DELETE FROM favorite WHERE destinationId = :destinationId")
    suspend fun deleteFavorite(destinationId: String)
}

