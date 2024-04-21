package com.marina.ruiz.globetrotting.data.network.firebase.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.local.booking.BookingEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookingResponse(
    val id: String,
    val agentName: String?,
    val agentId: String?,
    val clientName: String,
    val clientId: String,
    val destinationName: String,
    val destinationId: String,
    val end: Long,
    val start: Long,
    val amount: Float?,
    val isActive: Boolean,
    val isConfirmed: Boolean,
    val travelers: Int
) : Parcelable {

    fun asBookingEntity(): BookingEntity {
        return BookingEntity(
            id = id,
            agentId = agentId,
            clientId = clientId,
            destinationId = destinationId,
            end = end,
            start = start,
            amount = amount,
            isActive = isActive,
            isConfirmed = isConfirmed,
            travelers = travelers
        )
    }
}

fun List<BookingResponse>.asBookingEntityList(): List<BookingEntity> =
    this.map { it.asBookingEntity() }
