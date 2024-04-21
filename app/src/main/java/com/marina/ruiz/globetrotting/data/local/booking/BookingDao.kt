package com.marina.ruiz.globetrotting.data.local.booking

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createBookings(bookings: List<BookingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createBooking(booking: BookingEntity) {
        Log.d("GLOB_DEBUG BOOKING_DAO", "Creating booking: $booking")
    }

    @Query(
        "SELECT b.id AS id, a.name AS agentName, a.id AS agentId, u.name AS clientName, " +
                "u.uid as clientId, d.name AS destinationName, d.id AS destinationId, b." +
                "`end` AS `end`, b.start AS start, b.amount AS amount, b.isActive as isActive, " +
                "b.isConfirmed AS isConfirmed, b.travelers AS travelers " +
                "FROM booking AS b " +
                "INNER JOIN user AS u ON u.uid = b.clientId " +
                "LEFT JOIN agent AS a ON a.id = b.agentId " +
                "INNER JOIN destination AS d ON d.id = b.destinationId"
    )
    fun getAllBookings(): Flow<List<BookingClientAgentDestinationEntity>>

    @Update
    suspend fun updateBooking(bookingEntity: BookingEntity)

    @Query("DELETE FROM booking")
    suspend fun deleteBookings()
}

