package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.local.destination.DestinationEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    override val id: String = "",
    override val name: String = "",
    val type: String? = null,
    val dimension: String? = null,
    val price: Double = 0.0,
    val shortDescription: String = "",
    val description: String = "",
    override val imageRef: Int? = null,
    override val image: String? = null
) : Parcelable, SelectorItem {
    fun asDestinationEntity(): DestinationEntity {
        return DestinationEntity(
            id = id,
            name = name,
            type = type,
            dimension = dimension,
            price = price,
            shortDescription = shortDescription,
            description = description,
            //imageRef = imageRef
        )
    }
}

@Parcelize
data class Coordinate(
    val lng: Double,
    val lat: Double
) : Parcelable
