package com.marina.ruiz.globetrotting.data.network.firebase.model.payload

data class ProfilePayload(
    val name: String?, val surname: String?, val nickname: String
) {
    fun asHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name as Any,
            "surname" to surname as Any,
            "nickname" to nickname as Any
        )
    }
}
