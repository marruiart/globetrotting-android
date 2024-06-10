package com.marina.ruiz.globetrotting.data.local.destination

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Destination

@Entity(tableName = "destination")
data class DestinationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String?,
    val dimension: String?,
    val price: Double,
    val shortDescription: String?,
    val description: String?,
    //val imageRef: Int? = null
)

data class DestinationFavoritesEntity(
    val id: String,
    val name: String,
    val type: String?,
    val dimension: String?,
    val price: Double,
    val shortDescription: String?,
    val description: String?,
    val favId: String?
) {
    fun asDestination(): Destination {
        if (name == "France") {
            Log.d("GLOB_DEBUG AS DESTINATION", "${favId != null}")
        }
        return Destination(
            id = id,
            name = name,
            type = type,
            dimension = dimension,
            price = price,
            shortDescription = shortDescription ?: "",
            description = description ?: "",
            imageRef = chooseImage(type),
            favorite = favId != null
        )
    }
}

fun List<DestinationFavoritesEntity>.asDestinationList(): List<Destination> {
    return this.map { destination ->
        destination.asDestination()
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
        else -> R.drawable.home_background
    }
}
