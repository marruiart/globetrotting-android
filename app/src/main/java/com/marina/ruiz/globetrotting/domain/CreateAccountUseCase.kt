package com.marina.ruiz.globetrotting.domain

import com.marina.ruiz.globetrotting.ui.auth.model.UserCredentials
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import javax.inject.Inject


class CreateAccountUseCase @Inject constructor(
    private val authSvc: AuthService,
    private val userSvc: UserService
) {

    suspend operator fun invoke(credentials: UserCredentials): Boolean {
        val accountCreated =
            authSvc.createAccount(credentials.email, credentials.password) != null
        val user = authSvc.getCurrentUser()
        return if (accountCreated && user != null) {
            userSvc.createUserDocument(user.uid, credentials)
        } else {
            false
        }
    }
}