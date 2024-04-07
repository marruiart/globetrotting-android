package com.marina.ruiz.globetrotting.ui.main.profile.model

import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

data class ProfileForm(
    val tilName: TextInputEditText,
    val tilSurname: TextInputEditText,
    val tilNickname: TextInputEditText,
    val positiveBtn: Button,
    val neutralBtn: Button
)
