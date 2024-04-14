package com.marina.ruiz.globetrotting.data.local


/*@Entity(
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
    @PrimaryKey
    val id: String,
    val agentName: String,
    val agent_id: String,
    val booking_id: String,
    val clientName: String,
    val client_id: String,
    val destinationName: String,
    val destination_id: String,
    val end: Long,
    val start: Long,
    val isActive:Boolean,
    val isConfirmed: Boolean,
    val travelers: Int,
    val updatedAt: Long
)

data class FullBooking(
    val id: Int,
    val travelerName: String,
    val travelerImage: String,
    val destinationName: String,
    val type: String?,
    val dimension: String?,
    val price: Float,
    val shortDescription: String,
    val description: String,
    var departureDate: Long,
    var arrivalDate: Long,
    var numTravelers: Int
) *//*{
    fun asBooking(): Booking {
        return Booking(
            id,
            Traveler(
                name = travelerName,
                image = travelerImage
            ),
            Destination(
                id = id.toString(),
                name = destinationName,
                type = type,
                dimension = dimension,
                price = price,
                shortDescription = shortDescription,
                description = description,
                coordinate = Coordinate(0f, 0f),
                updatedAt = Timestamp.valueOf("10-01-1992"),
                imageRef = null
            ),
            departureDate,
            arrivalDate,
            numTravelers
        )
    }
}*/ // TODO

/*
fun List<FullBooking>.asFullBookingList(): List<Booking> {
    return this.map {
        it.asBooking()
    }
}*/ // TODO
