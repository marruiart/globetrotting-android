package com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model

import com.marina.ruiz.globetrotting.data.local.DestinationEntity


data class LocationApiModel(
    val id: Int = 0,
    val name: String,
    val type: String?,
    val dimension: String?,
    var price: Float = (50f + Math.random() * (500f - 50f)).toFloat(),
    var shortDescription: String = "",
    var description: String = ""
) {
    fun asDestinationEntity(): DestinationEntity {
        return DestinationEntity(
            id = id,
            name = name,
            type = type,
            dimension = dimension,
            price = price,
            shortDescription = shortDescription,
            description = description
        )
    }
}

fun List<LocationApiModel>.asEntityModelList(): List<DestinationEntity> {
    return this.map {
        it.asDestinationEntity()
    }
}