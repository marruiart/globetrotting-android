package com.marina.ruiz.globetrotting.ui.main.destinations.model

import com.google.firebase.Timestamp
import com.marina.ruiz.globetrotting.data.network.firebase.model.BookingPayload
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.User

data class BookingForm(
    val destination: Destination,
    var arrival: Timestamp = Timestamp(0L, 0),
    var departure: Timestamp = Timestamp(0L, 0),
    var travelers: Int = 0
) {
    fun toBookingPayload(user: User): BookingPayload {
        return BookingPayload(
            clientName = user.getFullName(),
            client_id = user.uid,
            destinationName = destination.name,
            destination_id = destination.id,
            end = arrival,
            start = departure,
            amount = 0f,
            travelers = travelers
        )
    }
}
