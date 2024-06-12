package com.marina.ruiz.globetrotting.domain

import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import javax.inject.Inject
import javax.inject.Singleton


interface ForgotPasswordListeners {
    /**
     * Callback method for when the password reminder is successfully sent.
     *
     * @param email The email address to which the password reminder was sent
     */
    fun onRemindPasswordSuccess(email: String)

    /**
     * Callback method for when the password reminder request fails.
     *
     * @param exception The exception that occurred during the request
     */
    fun onRemindPasswordFailure(exception: Exception)
}

@Singleton
class ForgotPasswordUseCase @Inject constructor(
    private val authSvc: AuthService
) {
    /**
     * Invokes the password reset process.
     *
     * @param email The email address for which the password reset is requested
     * @param callback The callback to handle success or failure responses
     */
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