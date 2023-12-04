package com.marina.ruiz.globetrotting.data.network.rickAndMortyApi.model

data class Pagination(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?,
)
