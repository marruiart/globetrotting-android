package com.marina.ruiz.globetrotting.data.repository.model

import com.marina.ruiz.globetrotting.data.local.BookingEntity

data class Booking(
    var traveler: Traveler,
    var destination: Destination,
    var departureDate: Long,
    var arrivalDate: Long,
    var numTravelers: Int
) {
    fun asBookingEntity(): BookingEntity {
        return BookingEntity(
            travelerId = traveler.id,
            destinationId = destination.id,
            departureDate = departureDate,
            arrivalDate = arrivalDate,
            numTravelers = numTravelers
        )
    }
}
