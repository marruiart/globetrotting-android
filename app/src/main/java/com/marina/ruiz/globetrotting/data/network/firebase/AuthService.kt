package com.marina.ruiz.globetrotting.data.network.firebase

import com.google.firebase.auth.AuthResult
import com.marina.ruiz.globetrotting.data.network.firebase.model.LoginResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthService @Inject constructor(private val firebase: FirebaseClient) {

    /**
     * Performs user authentication using the provided email and password.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @return A LoginResult object representing the result of the authentication attempt.
     *         This result encapsulates either a successful authentication or an error.
     * @throws Exception If an error occurs during the authentication process.
     */
    suspend fun login(email: String, password: String): LoginResult = runCatching {
        firebase.auth.signInWithEmailAndPassword(email, password).await()
    }.toLoginResult()

    /**
     * Creates a new user account with the provided email and password.
     *
     * @param email    The email address for the new user account.
     * @param password The password for the new user account.
     * @return An AuthResult object representing the result of the account creation attempt.
     *         This result encapsulates either a successful creation or an error.
     * @throws Exception If an error occurs during the account creation process.
     */
    suspend fun createAccount(email: String, password: String): AuthResult? {
        return firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    private fun Result<AuthResult>.toLoginResult() = when (val result = getOrNull()) {
        null -> LoginResult.Error
        else -> {
            val userId = result.user
            checkNotNull(userId)
            LoginResult.Success
        }
    }

}