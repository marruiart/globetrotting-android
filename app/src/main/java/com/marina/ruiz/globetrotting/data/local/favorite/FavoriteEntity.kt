package com.marina.ruiz.globetrotting.data.local.favorite

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.data.local.destination.DestinationEntity
import com.marina.ruiz.globetrotting.data.local.user.UserEntity
import com.marina.ruiz.globetrotting.data.repository.model.Favorite


@Entity(
    tableName = "favorite",
    foreignKeys = [
        ForeignKey(
            entity = DestinationEntity::class,
            parentColumns = ["id"],
            childColumns = ["destinationId"]
        )
    ]
)
data class FavoriteEntity(
    @PrimaryKey
    val id: String,
    val destinationId: String
) {
    fun asFavorite(): Favorite {
        return Favorite(id, destinationId)
    }
}

fun List<FavoriteEntity>.asFavoritesList(): List<Favorite> {
    return this.map { fav -> fav.asFavorite() }
}