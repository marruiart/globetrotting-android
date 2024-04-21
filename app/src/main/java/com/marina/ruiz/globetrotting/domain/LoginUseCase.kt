package com.marina.ruiz.globetrotting.domain

import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authSvc: AuthService,
    private val userSvc: UserService
) {

    suspend operator fun invoke(email: String, password: String): LoginResult =
        authSvc.login("client@gmail.com", "Aa123456") // TODO use parameters

}