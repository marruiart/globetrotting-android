package com.marina.ruiz.globetrotting.data.network.firebase.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.local.destination.DestinationEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class DestinationResponse(
    val id: String,
    val name: String,
    val type: String,
    val dimension: String,
    val price: Double,
    val shortDescription: String,
    val description: String
/*    val imageRef: Int?,
    val image: String?*/
) : Parcelable {

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

fun List<DestinationResponse>.asDestinationsEntityList(): List<DestinationEntity> {
    return this.map { it.asDestinationEntity() }
}
