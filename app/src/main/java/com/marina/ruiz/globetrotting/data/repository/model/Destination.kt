package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.DestinationPayload
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    override val id: String = "",
    override val name: String = "",
    val type: String? = null,
    val keywords: ArrayList<String>? = null,
    val price: Double = 0.0,
    var shortDescription: String = "",
    var description: String = "",
    val country: String = "",
    val continent: String = "",
    override val imageRef: Int? = null,
    override val image: String? = null,
    var favorite: Boolean = false
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Destination) return false

        return id == other.id && name == other.name && type == other.type && keywords == other.keywords && price == other.price && shortDescription == other.shortDescription && description == other.description && imageRef == other.imageRef && image == other.image
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (keywords?.hashCode() ?: 0)
        result = 31 * result + price.hashCode()
        result = 31 * result + shortDescription.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (imageRef?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        // 'favorite' is not included in hashCode calculation
        return result
    }
}

@Parcelize
data class Coordinate(
    val lng: Double, val lat: Double
) : Parcelable
