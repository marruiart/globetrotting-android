package com.marina.ruiz.globetrotting.domain

import com.google.firebase.auth.FirebaseUser
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authSvc: AuthService,
    private val userSvc: UserService
) {

    suspend operator fun invoke(email: String, password: String): LoginResult =
        authSvc.login(email, password)

    suspend fun getUserRole(): String? {
        return userSvc.getUserRole()
    }
}