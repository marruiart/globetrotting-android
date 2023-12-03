package com.marina.ruiz.globetrotting.data.repository.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Traveler(
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String
) : Parcelable
