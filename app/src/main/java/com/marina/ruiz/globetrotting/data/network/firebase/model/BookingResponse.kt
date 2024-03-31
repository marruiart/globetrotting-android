package com.marina.ruiz.globetrotting.data.network.firebase.model

data class BookingResponse(
    val id: String,
    val start: String,
    val end: String,
    val travelers: Int,
    val rating: Int?,
    val isActive: Boolean,
    val isConfirmed: Boolean,
    val agent_id: String?,
    val agentName: String?,
    val client_id: String,
    val clientName: String?,
    val destination_id: String,
    val destinationName: String?,
    val updatedAt: String?,
    val createdAt: String?
)
