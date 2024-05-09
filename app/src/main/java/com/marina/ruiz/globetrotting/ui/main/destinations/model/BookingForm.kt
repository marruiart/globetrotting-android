package com.marina.ruiz.globetrotting.ui.main.destinations.model

import com.google.firebase.Timestamp
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.BookingPayload
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.User

data class BookingForm(
    val destination: Destination,
    var arrival: Timestamp = Timestamp(0L, 0),
    var departure: Timestamp = Timestamp(0L, 0),
    var travelers: Int = 1,
    var nights: Int = 0,
    var amount: Double = destination.price
) {
    fun toBookingPayload(user: User): BookingPayload {
        return BookingPayload(
            clientName = user.getFullName(),
            client_id = user.uid,
            destinationName = destination.name,
            destination_id = destination.id,
            end = arrival,
            start = departure,
            amount = amount,
            nights = nights,
            travelers = travelers
        )
    }
}
