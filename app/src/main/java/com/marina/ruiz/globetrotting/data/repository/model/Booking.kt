package com.marina.ruiz.globetrotting.data.repository.model

import com.marina.ruiz.globetrotting.data.local.BookingEntity

data class Booking(
    var travelerId: Int,
    var destinationId: Int,
    var departureDate: Long,
    var arrivalDate: Long,
    var numTravelers: Int
) {
    fun asBookingEntity(): BookingEntity {
        return BookingEntity(
            travelerId = travelerId,
            destinationId = destinationId,
            departureDate = departureDate,
            arrivalDate = arrivalDate,
            numTravelers = numTravelers
        )
    }
}
