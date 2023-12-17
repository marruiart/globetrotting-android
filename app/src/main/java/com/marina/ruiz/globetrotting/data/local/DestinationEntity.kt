package com.marina.ruiz.globetrotting.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Destination

@Entity(tableName = "destination")
data class DestinationEntity(
    @PrimaryKey()
    val id: Int,
    val name: String,
    val type: String?,
    val dimension: String?,
    val price: Float,
    val shortDescription: String,
    val description: String,
    val fav: Boolean = false,
    val imageRef: Int? = null
) {
    fun asDestination(): Destination {
        return Destination(
            id,
            name,
            type,
            dimension,
            price,
            shortDescription,
            description,
            fav,
            chooseImage(type)
        )
    }
}

fun List<DestinationEntity>.asDestinationList(): List<Destination> {
    return this.map {
        it.asDestination()
    }
}

private fun chooseImage(type: String?): Int {
    return when (type) {
        "Planet" -> R.drawable.type_planet
        "Cluster" -> R.drawable.type_cluster
        "Space station" -> R.drawable.type_space_station
        "Microverse" -> R.drawable.type_microverse
        "TV" -> R.drawable.type_tv
        "Resort" -> R.drawable.type_resort
        "Fantasy town" -> R.drawable.type_fantasy
        "Dream" -> R.drawable.type_dream
        "Dimension" -> R.drawable.type_dimension
        "Menagerie" -> R.drawable.type_menagerie
        "Game" -> R.drawable.type_game
        //"Customs" -> R.drawable.type_customs
        "Daycare" -> R.drawable.type_daycare
        //"Dwarf planet (Celestial Dwarf)" -> R.drawable.type_dwarf
        "Miniverse" -> R.drawable.type_miniverse
        //"Teenyverse" -> R.drawable.type_teenyverse
        "Box" -> R.drawable.type_box
        "Spacecraft" -> R.drawable.type_spacecraft
        //"Artificially generated world" -> R.drawable.type_artifical
        //"Machine" -> R.drawable.type_machine
        //"Arcade" -> R.drawable.type_arcade
        //"Spa" -> R.drawable.type_spa
        else -> R.drawable.default_avatar
    }
}
