package com.marina.ruiz.globetrotting.data.network.firebase.model.payload

import com.google.firebase.Timestamp

data class DestinationPayload(
    val id: String,
    val shortDescription: String,
    val description: String,
    val updatedAt: Timestamp = Timestamp.now()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "shortDescription" to shortDescription,
            "description" to description,
            "updatedAt" to updatedAt
        )
    }
}