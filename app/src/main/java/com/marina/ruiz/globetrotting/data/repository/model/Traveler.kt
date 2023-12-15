package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Traveler(
    override val id: Int,
    override val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    override val image: String,
    override val imageRef: Int? = null
) : Parcelable, SelectorItem