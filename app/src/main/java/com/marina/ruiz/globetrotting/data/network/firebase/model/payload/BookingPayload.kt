package com.marina.ruiz.globetrotting.data.network.firebase.model.payload

import com.google.firebase.Timestamp

data class BookingPayload(
    val id: String = Payload.generateId(),
    val clientName: String,
    val client_id: String,
    val destinationName: String,
    val destination_id: String,
    val end: Timestamp,
    val start: Timestamp,
    val amount: Float,
    val isActive: Boolean = true,
    val isConfirmed: Boolean = false,
    val travelers: Int,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "clientName" to clientName,
            "client_id" to client_id,
            "destinationName" to destinationName,
            "destination_id" to destination_id,
            "end" to end,
            "start" to start,
            "amount" to amount,
            "isActive" to isActive,
            "isConfirmed" to isConfirmed,
            "travelers" to travelers,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt
        )
    }
}