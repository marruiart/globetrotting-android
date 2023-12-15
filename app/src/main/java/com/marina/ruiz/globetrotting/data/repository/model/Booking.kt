package com.marina.ruiz.globetrotting.data.repository.model

import java.util.Date

data class Booking(
    var travelerId: Int,
    var destinationId: Int,
    var departureDate: Date,
    var arrivalDate: Date,
    var numTravelers: Int
)
