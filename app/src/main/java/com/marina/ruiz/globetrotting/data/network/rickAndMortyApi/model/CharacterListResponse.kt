package com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model

data class CharacterListResponse(
    val info: Pagination,
    val results: List<CharacterResponse>
)

data class Pagination(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?,
)

data class CharacterResponse(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String
) {
    fun asApiModel(): CharacterApiModel {
        return CharacterApiModel(
            id,
            name,
            status,
            species,
            type,
            gender,
            image
        )
    }
}
