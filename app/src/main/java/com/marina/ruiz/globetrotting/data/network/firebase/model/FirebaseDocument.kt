package com.marina.ruiz.globetrotting.data.network.firebase.model

import com.marina.ruiz.globetrotting.data.local.DestinationEntity
import com.marina.ruiz.globetrotting.data.repository.model.Coordinate

data class FirebaseDocument(
    val id: String,
    val data: DocumentData
) {
    fun asDestinationEntity(): DestinationEntity {
        return DestinationEntity(
            id = id,
            name = data["name"] as String,
            type = data["type"] as String?,
            dimension = data["dimension"] as String?,
            price = data["price"] as Double,
            shortDescription = data.getOrDefault("shortDescription", "") as String,
            description = data.getOrDefault("description", "") as String,
            imageRef = null
        )
    }
}

typealias DocumentData = Map<String, Any?>


fun List<FirebaseDocument>.asDestinationEntityList(): List<DestinationEntity> {
    return this.map {
        it.asDestinationEntity()
    }
}
