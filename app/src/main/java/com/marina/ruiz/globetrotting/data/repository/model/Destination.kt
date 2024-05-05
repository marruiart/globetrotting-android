package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.DestinationPayload
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    override val id: String = "",
    override val name: String = "",
    val type: String? = null,
    val dimension: String? = null,
    val price: Double = 0.0,
    var shortDescription: String = "",
    var description: String = "",
    override val imageRef: Int? = null,
    override val image: String? = null
) : Parcelable, SelectorItem {
    fun asDestinationPayload(
        newDescription: String? = null, newShortDescription: String? = null
    ): DestinationPayload {
        return DestinationPayload(
            id = id,
            shortDescription = newShortDescription ?: shortDescription,
            description = newDescription ?: description,
            //imageRef = imageRef
        )
    }
}

@Parcelize
data class Coordinate(
    val lng: Double, val lat: Double
) : Parcelable
