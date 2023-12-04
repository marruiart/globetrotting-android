package com.marina.ruiz.globetrotting.data.repository.model

data class Destination(
    val name: String,
    val type: String?,
    val dimension: String?,
    val image: String?,
    val price: Float?,
    val description: String?,
    val fav: Boolean = false
)
