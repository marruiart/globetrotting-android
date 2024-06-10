package com.marina.ruiz.globetrotting.data.repository.model

import com.marina.ruiz.globetrotting.data.local.favorite.FavoriteEntity

data class Favorite(
    val id: String, val destinationId: String
) {
    fun asEntity(): FavoriteEntity {
        return FavoriteEntity(
            id, destinationId
        )
    }
}

fun List<Favorite>.asEntityList(): List<FavoriteEntity> {
    return this.map { it.asEntity() }
}

