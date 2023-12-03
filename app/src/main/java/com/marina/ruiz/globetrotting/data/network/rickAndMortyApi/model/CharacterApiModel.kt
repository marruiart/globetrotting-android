package com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model

import com.marina.ruiz.globetrotting.data.local.TravelerEntity

data class CharacterApiModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String
) {
    fun asTravelerEntity(): TravelerEntity {
        return TravelerEntity(
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

fun List<CharacterApiModel>.asEntityModelList(): List<TravelerEntity> {
    return this.map {
        it.asTravelerEntity()
    }
}