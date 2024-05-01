package com.marina.ruiz.globetrotting.domain

import com.marina.ruiz.globetrotting.data.network.firebase.BookingsService
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.BookingPayload
import javax.inject.Inject

class BookNowUseCase @Inject constructor(
    private val bookingsSvc: BookingsService
) {

    suspend operator fun invoke(booking: BookingPayload) = bookingsSvc.createBooking(booking)

}