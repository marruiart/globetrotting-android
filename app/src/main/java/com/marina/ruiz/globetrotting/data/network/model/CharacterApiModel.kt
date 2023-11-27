package com.marina.ruiz.globetrotting.data.network.model

data class CharacterApiModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String
)

data class CharacterListApiModel(
    val list: List<CharacterApiModel>
)