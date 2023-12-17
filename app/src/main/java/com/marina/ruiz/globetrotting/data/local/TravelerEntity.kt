package com.marina.ruiz.globetrotting.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.data.repository.model.Traveler

@Entity(tableName = "traveler")
data class TravelerEntity(
    @PrimaryKey()
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val description: String = ""
) {
    fun asTraveler(): Traveler {
        return Traveler(
            id,
            name,
            status,
            species,
            type,
            gender,
            image,
            description
        )
    }
}

fun List<TravelerEntity>.asTravelerList(): List<Traveler> {
    // this is referred to List<TravelerEntity>
    return this.map {
        it.asTraveler()
    }
}