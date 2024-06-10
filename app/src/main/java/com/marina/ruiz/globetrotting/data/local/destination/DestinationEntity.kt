package com.marina.ruiz.globetrotting.data.local.destination

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Destination

@Entity(tableName = "destination")
data class DestinationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String?,
    val keywords: String,
    val price: Double,
    val country: String?,
    val continent: String?,
    val shortDescription: String?,
    val description: String?,
    //val imageRef: Int? = null
)

data class DestinationFavoritesEntity(
    val id: String,
    val name: String,
    val type: String?,
    val keywords: String,
    val price: Double,
    val country: String?,
    val continent: String?,
    val shortDescription: String?,
    val description: String?,
    val favId: String?
) {
    fun asDestination(): Destination {
        return Destination(
            id = id,
            name = name,
            type = type,
            keywords = keywords.split(",").toCollection(ArrayList()),
            price = price,
            country = country ?: "",
            continent = continent ?: "",
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

private fun chooseImage(continent: String?): Int {
    return when (continent) {
        "Asia" -> R.drawable.type_planet
        "Africa" -> R.drawable.type_cluster
        "North America" -> R.drawable.type_space_station
        "South America" -> R.drawable.type_microverse
        "Oceania" -> R.drawable.type_tv
        "Europe" -> R.drawable.type_tv
        else -> R.drawable.placeholder
    }
}
