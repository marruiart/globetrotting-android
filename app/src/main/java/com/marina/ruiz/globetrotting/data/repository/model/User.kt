package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String,
    val username: String,
    val nickname: String,
    val email: String,
    //val avatar: Any? = null,
    val name: String? = null,
    val surname: String? = null,
    val age: String? = null
) : Parcelable {
    fun getFullName(): String {
        return if (name == null) nickname else "${name}${if (surname != null) (" " + surname) else ""}"
    }
}