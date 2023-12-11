package com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model

import com.marina.ruiz.globetrotting.data.local.DestinationEntity


data class LocationApiModel(
    val id: Int = 0,
    val name: String,
    val type: String?,
    val dimension: String?,
    val price: Float?,
    val description: String?
) {
    fun asDestinationEntity(): DestinationEntity {
        return DestinationEntity(
            name = name,
            type = type,
            dimension = dimension,
            price = price,
            description = description
        )
    }
}

fun List<LocationApiModel>.asEntityModelList(): List<DestinationEntity> {
    return this.map {
        it.asDestinationEntity()
    }
}