package com.marina.ruiz.globetrotting.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.data.repository.model.Destination

@Entity(tableName = "destination")
data class DestinationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String?,
    val dimension: String?,
    val price: Float?,
    val description: String?,
    val fav: Boolean = false
) {
    fun asDestination(): Destination {
        return Destination(
            name,
            type,
            dimension,
            price,
            description,
            fav
        )
    }
}

fun List<DestinationEntity>.asDestinationList(): List<Destination> {
    return this.map {
        it.asDestination()
    }
}
