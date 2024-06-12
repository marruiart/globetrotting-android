package com.marina.ruiz.globetrotting.domain

import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import javax.inject.Inject
import javax.inject.Singleton


interface ForgotPasswordListeners {
    fun onRemindPasswordSuccess(email: String)
    fun onRemindPasswordFailure(exception: Exception)
}

@Singleton
class ForgotPasswordUseCase @Inject constructor(
    private val authSvc: AuthService
) {
    operator fun invoke(email: String, callback: ForgotPasswordListeners) = run {
        try {
            authSvc.resetPassword(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback.onRemindPasswordSuccess(email)
                } else {
                    callback.onRemindPasswordFailure(Exception("On complete error"))
                }
            }
        } catch (ex: Exception) {
            callback.onRemindPasswordFailure(ex)
        }
    }

}