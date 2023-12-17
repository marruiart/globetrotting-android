package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    override val id: Int = 0,
    override val name: String,
    val type: String?,
    val dimension: String?,
    val price: Float,
    val shortDescription: String,
    val description: String,
    val fav: Boolean = false,
    override val imageRef: Int? = null,
    override val image: String? = null
) : Parcelable, SelectorItem