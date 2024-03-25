package com.marina.ruiz.globetrotting.domain

import com.marina.ruiz.globetrotting.data.repository.model.UserCredentials
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import javax.inject.Inject


class CreateAccountUseCase @Inject constructor(
    private val authenticationSvc: AuthService,
    private val userSvc: UserService
) {

    suspend operator fun invoke(credentials: UserCredentials): Boolean {
        val accountCreated =
            authenticationSvc.createAccount(credentials.email, credentials.password) != null
        return if (accountCreated) {
            userSvc.createUserTable(credentials)
        } else {
            false
        }
    }
}