package com.marina.ruiz.globetrotting.data.local

import androidx.room.Dao
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

    @Query("SELECT b.id AS id, b.departureDate AS departureDate, b.arrivalDate AS arrivalDate, b.numTravelers AS numTravelers, " +
            "t.name AS travelerName, t.image AS travelerImage, " +
            "d.name AS destinationName, d.type AS destinationType, d.dimension AS destinationDimension, d.price AS destinationPrice, " +
            "d.description AS destinationDescription " +
            "FROM booking AS b " +
            "INNER JOIN traveler AS t ON t.id = b.travelerId " +
            "INNER JOIN destination AS d ON d.id = b.destinationId")
    fun getAllBookingsWithTravelerAndDestination(): Flow<List<FullBooking>>

//    @Query("SELECT * FROM traveler WHERE id = :travelerId")
//    fun getDestinationsForTraveler(travelerId: Int): Flow<List<DestinationEntity>>
//
//    @Query("SELECT * FROM destination WHERE id = :destinationId")
//    fun getTravelersForDestination(destinationId: Int): Flow<List<TravelerEntity>>
}