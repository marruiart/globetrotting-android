package com.marina.ruiz.globetrotting.data.network.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.marina.ruiz.globetrotting.data.network.firebase.model.result.LoginResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(val firebase: FirebaseService) {

    companion object {
        private const val TAG = "GLOB_DEBUG AUTH_SERVICE"
    }

    private var authStateListener: FirebaseAuth.AuthStateListener? = null
    private val _uid = MutableLiveData<String?>()
    val uid: LiveData<String?>
        get() = _uid

    init {
        startListening()
    }

    fun startListening() {
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            Log.i(TAG, "Listening uid: ${auth.currentUser?.uid}")
            _uid.value = auth.currentUser?.uid
        }
        firebase.client.auth.addAuthStateListener(authStateListener!!)
    }

    fun stopListening() {
        authStateListener?.let {
            firebase.client.auth.removeAuthStateListener(it)
            authStateListener = null
        }
    }

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
        firebase.client.auth.signInWithEmailAndPassword(email, password).await()
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
        return firebase.client.auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun resetPassword(email: String): Task<Void> {
        return firebase.client.auth.sendPasswordResetEmail(email)
    }

    fun logout() = runCatching {
        firebase.logout()
    }

    private fun Result<AuthResult>.toLoginResult() = when (val result = getOrNull()) {
        null -> LoginResult.Error
        else -> {
            checkNotNull(result.user)
            LoginResult.Success
        }
    }

}