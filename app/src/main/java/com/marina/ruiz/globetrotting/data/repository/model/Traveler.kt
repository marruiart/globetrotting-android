package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Traveler(
    override val id: Int = 0,
    override val name: String = "",
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    override val image: String = "",
    val description: String = "",
    override val imageRef: Int? = null
) : Parcelable, SelectorItem