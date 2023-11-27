package com.marina.ruiz.globetrotting.data.network.model

data class CharacterListResponse(
    val info: Pagination,
    val results: List<CharacterDetailResponse>
)

data class CharacterDetailResponse(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String
)

data class Pagination(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?,
)