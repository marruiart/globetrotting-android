package com.marina.ruiz.globetrotting.ui.auth

data class UserLogin(
    val email: String = "",
    val password: String = "",
    val showErrorDialog: Boolean = false
)
