package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.local.BookingEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    val id: Int = 0,
    var traveler: Traveler = Traveler(),
    var destination: Destination = Destination(),
    var departureDate: Long = 0,
    var arrivalDate: Long = 0,
    var numTravelers: Int = 0
) : Parcelable {
    fun asBookingEntity(): BookingEntity {
        return BookingEntity(
            id = id,
            travelerId = traveler.id,
            destinationId = destination.id,
            departureDate = departureDate,
            arrivalDate = arrivalDate,
            numTravelers = numTravelers
        )
    }
}
