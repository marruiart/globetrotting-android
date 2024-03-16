package com.marina.ruiz.globetrotting.ui.auth

data class AuthViewState(
    val isLoading: Boolean = false,
    val isValidEmail: Boolean = true,
    val isValidPassword: Boolean = true
)
