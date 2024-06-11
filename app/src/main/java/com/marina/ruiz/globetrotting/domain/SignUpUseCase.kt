package com.marina.ruiz.globetrotting.domain

import com.google.firebase.auth.AuthResult
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import javax.inject.Inject


interface SignUpListeners {
    fun onSignUpSuccess(username: String)
    fun onSignUpFailure(exception: Exception)
}


class SignUpUseCase @Inject constructor(
    private val authSvc: AuthService, private val userSvc: UserService
) {

    suspend operator fun invoke(
        username: String, email: String, password: String, callback: SignUpListeners
    ) {
        var userResult: AuthResult? = null
        try {
            authSvc.stopListening()
            userResult = authSvc.createAccount(email, password)
        } catch (ex: Exception) {
            callback.onSignUpFailure(ex)
        }
        val user = userResult?.user
        if (user != null) {
            var res = userSvc.createUserDocument(user.uid, username, email)
            if (res) {
                callback.onSignUpSuccess(username)
                authSvc.startListening()
            } else {
                callback.onSignUpFailure(Exception("Error at creating document"))
                authSvc.startListening()
            }
        } else {
            callback.onSignUpFailure(Exception("Error"))
        }
    }
}