package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.local.booking.BookingEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class Booking(
    val id: String,
    val agentName: String?,
    val agentId: String?,
    val clientName: String,
    val clientId: String,
    val destinationName: String,
    val destinationId: String,
    val end: Long,
    val start: Long,
    val amount: Float,
    val isActive: Boolean,
    val isConfirmed: Boolean,
    val travelers: Int
) : Parcelable
