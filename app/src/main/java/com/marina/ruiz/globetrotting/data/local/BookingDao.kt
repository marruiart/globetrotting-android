package com.marina.ruiz.globetrotting.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert
    suspend fun createBooking(booking: BookingEntity)

    @Query("SELECT * FROM booking")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query(
        "SELECT b.id AS id, b.departureDate AS departureDate, b.arrivalDate AS arrivalDate, b.numTravelers AS numTravelers, " +
                "t.name AS travelerName, t.image AS travelerImage, " +
                "d.name AS destinationName, d.type AS type, d.dimension AS dimension, d.price AS price, " +
                "d.description AS description, d.shortDescription AS shortDescription " +
                "FROM booking AS b " +
                "INNER JOIN traveler AS t ON t.id = b.travelerId " +
                "INNER JOIN destination AS d ON d.id = b.destinationId"
    )
    fun getAllBookingsWithTravelerAndDestination(): Flow<List<FullBooking>>

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)
}