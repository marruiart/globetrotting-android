package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Agent(
    val uid: String,
    val username: String,
    val nickname: String,
    val email: String,
    //val avatar: Any? = null,
    val name: String? = null,
    val surname: String? = null,
) : Parcelable