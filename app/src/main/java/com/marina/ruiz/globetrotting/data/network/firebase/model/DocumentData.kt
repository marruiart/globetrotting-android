package com.marina.ruiz.globetrotting.data.network.firebase.model

import android.util.Log
import com.google.firebase.Timestamp
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.AgentResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.BookingResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.DestinationResponse

typealias DocumentData = Map<String, Any?>

fun DocumentData.asAgentResponse(): AgentResponse {
    return AgentResponse(
        this["user_id"] as String,
        this["username"] as String,
        this["nickname"] as String,
        this["email"] as String,
        //this["avatar"],
        this["name"] as String?,
        this["surname"] as String?
    )
}

fun DocumentData.asDestinationResponse(): DestinationResponse {
    return DestinationResponse(
        this["id"] as String,
        this["name"] as String,
        this["type"] as? String ?: "",
        this["keywords"] as? String ?: "",
        this["price"] as Double,
        this["country"] as String?,
        this["continent"] as String?,
        this["shortDescription"] as? String ?: "",
        this["description"] as? String ?: ""
    )
}

fun DocumentData.asBookingResponse(): BookingResponse {
    Log.d("GLOB_DEBUG asBookingResponse", this["id"] as String)
    return BookingResponse(
        this["id"] as String,
        this["agentName"] as? String,
        this["agent_id"] as? String,
        this["clientName"] as String,
        this["client_id"] as String,
        this["destinationName"] as String,
        this["destination_id"] as String,
        (this["end"] as Timestamp).toDate().time,
        (this["start"] as Timestamp).toDate().time,
        this["amount"] as? Float,
        this["isActive"] as Boolean,
        this["isConfirmed"] as Boolean,
        (this["travelers"] as Long).toInt()
    )
}