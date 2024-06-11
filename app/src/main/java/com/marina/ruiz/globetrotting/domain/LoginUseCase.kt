package com.marina.ruiz.globetrotting.domain

import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.result.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authSvc: AuthService
) {

    suspend operator fun invoke(email: String, password: String): LoginResult =
        authSvc.login(email, password)

}