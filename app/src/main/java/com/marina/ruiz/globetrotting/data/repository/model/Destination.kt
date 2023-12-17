package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.local.DestinationEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    override val id: Int = 0,
    override val name: String = "",
    val type: String? = null,
    val dimension: String? = null,
    val price: Float = 0f,
    val shortDescription: String = "",
    val description: String = "",
    val fav: Boolean = false,
    override val imageRef: Int? = null,
    override val image: String? = null
) : Parcelable, SelectorItem {
    fun asDestinationEntity(): DestinationEntity {
        return DestinationEntity(
            id,
            name,
            type,
            dimension,
            price,
            shortDescription,
            description,
            fav,
            imageRef
        )
    }
}