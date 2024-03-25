package com.marina.ruiz.globetrotting.data.network.firebase.model

sealed class LoginResult {
    data object Error : LoginResult()
    data object Success : LoginResult()
}