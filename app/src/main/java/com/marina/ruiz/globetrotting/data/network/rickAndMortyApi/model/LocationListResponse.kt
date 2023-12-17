package com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model

data class LocationListResponse(
    val info: Pagination,
    val results: List<LocationResponse>
)

data class LocationResponse(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
) {
    fun asApiModel(imageUrl: String = "https://source.unsplash.com/1440x960/travel"): LocationApiModel {
        return LocationApiModel(
            id,
            name,
            type,
            dimension
        )
    }
}

