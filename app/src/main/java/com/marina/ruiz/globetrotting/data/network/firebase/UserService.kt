package com.marina.ruiz.globetrotting.data.network.firebase

import android.util.Log
import com.marina.ruiz.globetrotting.data.repository.model.UserCredentials
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserService @Inject constructor(private val firebase: FirebaseClient) {

    companion object {
        const val USER_COLLECTION = "users"
    }

    suspend fun createUserTable(credentials: UserCredentials) = runCatching {

        val user = hashMapOf(
            "email" to credentials.email,
            "username" to credentials.username,
            "nickname" to credentials.username,
            "role" to "AUTHENTICATED",
            "user_id" to generateId(),
            "favorites" to emptyArray<Any>()
        )

        firebase.db
            .collection(USER_COLLECTION)
            .add(user).await()

    }.isSuccess

    suspend fun getUserRole(): String? {
        val currentUser = firebase.auth.currentUser ?: return null
        val docRef = firebase.db.collection(USER_COLLECTION).document(currentUser.uid)
        try {
            val document = docRef.get().await()
            return document?.data?.get("role") as String?
        } catch (e: Exception) {
            Log.d("ROLE", "Error getting document: $e")
            return null
        }
    }

    private fun generateId(): String {
        val firstPart = (Math.random() * 1000000000000000).toLong().toString(36)
        val secondPart = (Math.random() * 1000000000000000).toLong().toString(36)
        return firstPart.substring(0, 13) + secondPart.substring(0, 13)
    }

}