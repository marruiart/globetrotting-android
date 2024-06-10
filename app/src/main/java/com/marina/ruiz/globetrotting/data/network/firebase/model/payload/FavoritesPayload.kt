package com.marina.ruiz.globetrotting.data.network.firebase.model.payload

import com.marina.ruiz.globetrotting.data.repository.model.Favorite

data class FavoritesPayload(
    val favorites: List<Favorite>
) {
    fun asHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "favorites" to (favorites.map { fav ->
                hashMapOf(
                    "destination_id" to fav.destinationId,
                    "fav_id" to fav.id
                )
            }) as Any,
        )
    }
}
