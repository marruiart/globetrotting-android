package com.marina.ruiz.globetrotting.data.network.firebase.model.payload

import com.google.firebase.firestore.FieldValue

data class ProfilePayload(
    val name: String?,
    val surname: String?,
    val nickname: String,
    var avatar: String? = ""
) {
    fun asHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name as Any,
            "surname" to surname as Any,
            "nickname" to nickname as Any,
            "avatar" to if (avatar != null) avatar as Any else FieldValue.delete()
        )
    }
}
