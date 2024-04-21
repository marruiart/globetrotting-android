package com.marina.ruiz.globetrotting.data.local.booking

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.data.local.agent.AgentEntity
import com.marina.ruiz.globetrotting.data.local.user.UserEntity
import com.marina.ruiz.globetrotting.data.repository.model.Booking


@Entity(
    tableName = "booking",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"]
        ),
        /*        ForeignKey(
                    entity = DestinationEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["destinationId"]
                )*/
        ForeignKey(
            entity = AgentEntity::class,
            parentColumns = ["id"],
            childColumns = ["agentId"]
        ),
    ],
    indices = [
        Index(value = ["clientId"]),
        //Index(value = ["destinationId"])
        Index(value = ["agentId"])
    ]
)
data class BookingEntity(
    @PrimaryKey
    val id: String,
    val agentId: String?,
    val clientId: String,
    //val destinationId: String,
    val end: Long,
    val start: Long,
    val amount: Float?, // FIXME remove nullable
    val isActive: Boolean,
    val isConfirmed: Boolean,
    val travelers: Int
)

data class BookingClientAgentDestinationEntity(
    val id: String,
    val agentName: String,
    val agentId: String,
    val clientName: String,
    val clientId: String,
    //val destinationName: String,
    //val destinationId: String,
    val end: Long,
    val start: Long,
    val amount: Float,
    val isActive: Boolean,
    val isConfirmed: Boolean,
    val travelers: Int
) {
    fun asBooking(): Booking {
        return Booking(
            id = id,
            agentName = agentName,
            agentId = agentId,
            clientName = clientName,
            clientId = clientId,
            destinationName = "destinationName",
            destinationId = "destinationId",
            end = end,
            start = start,
            amount = amount,
            isActive = isActive,
            isConfirmed = isConfirmed,
            travelers = travelers
        )
    }
}

fun List<BookingClientAgentDestinationEntity>.asBookingList(): List<Booking> =
    this.map { it.asBooking() }

