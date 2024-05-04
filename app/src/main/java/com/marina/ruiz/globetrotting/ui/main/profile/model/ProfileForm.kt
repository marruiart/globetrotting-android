package com.marina.ruiz.globetrotting.ui.main.profile.model

import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.ProfilePayload


data class ProfileForm(
    val username: String,
    val email: String,
    val name: String?,
    val surname: String?,
    val nickname: String,
    val avatar: String?
) {
    fun toProfilePayload(): ProfilePayload {
        return ProfilePayload(
            name = name,
            surname = surname,
            nickname = nickname,
            avatar = avatar
        )
    }
}

