package com.marina.ruiz.globetrotting.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import java.util.Date

@Entity(
    tableName = "booking",
    foreignKeys = [
        ForeignKey(
            entity = TravelerEntity::class,
            parentColumns = ["id"],
            childColumns = ["travelerId"]
        ),
        ForeignKey(
            entity = DestinationEntity::class,
            parentColumns = ["id"],
            childColumns = ["destinationId"]
        )
    ],
    indices = [
        Index(value = ["travelerId"]),
        Index(value = ["destinationId"])
    ]
)
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val travelerId: Int,
    val destinationId: Int,
    var departureDate: Long,
    var arrivalDate: Long,
    var numTravelers: Int
)

data class FullBooking(
    val id: Int,
    val travelerName: String,
    val travelerImage: String,
    val destinationName: String,
    val destinationType: String?,
    val destinationDimension: String?,
    val destinationPrice: Float?,
    val destinationDescription: String?,
    var departureDate: Long,
    var arrivalDate: Long,
    var numTravelers: Int
) {
    fun asBooking(): Booking {
        return Booking(
            Traveler(
                name = travelerName,
                image = travelerImage
            ),
            Destination(
                name = destinationName,
                type = destinationType,
                dimension = destinationDimension,
                price = destinationPrice,
                description = destinationDescription
            ),
            departureDate,
            arrivalDate,
            numTravelers
        )
    }
}

fun List<FullBooking>.asFullBookingList(): List<Booking> {
    return this.map {
        it.asBooking()
    }
}