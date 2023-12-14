package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    val name: String,
    val type: String?,
    val dimension: String?,
    val price: Float?,
    val description: String?,
    val fav: Boolean = false,
    val imageRef: Int
) : Parcelable
