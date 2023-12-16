package com.marina.ruiz.globetrotting.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert
    suspend fun createBooking(booking: BookingEntity)

    @Query("SELECT * FROM booking")
    fun getAllBookings(): Flow<List<BookingEntity>>

//    @Query("SELECT * FROM traveler WHERE id = :travelerId")
//    fun getDestinationsForTraveler(travelerId: Int): Flow<List<DestinationEntity>>
//
//    @Query("SELECT * FROM destination WHERE id = :destinationId")
//    fun getTravelersForDestination(destinationId: Int): Flow<List<TravelerEntity>>
}
