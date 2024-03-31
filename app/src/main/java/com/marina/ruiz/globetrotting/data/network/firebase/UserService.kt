package com.marina.ruiz.globetrotting.data.network.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marina.ruiz.globetrotting.data.network.firebase.model.UserDataResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.asUserDataResponse
import com.marina.ruiz.globetrotting.ui.auth.model.UserCredentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor(private val firebase: FirebaseService) {

    private val _userData = MutableStateFlow<UserDataResponse?>(null)
    val userData: StateFlow<UserDataResponse?>
        get() = _userData

    private val _logout: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val logout: StateFlow<Boolean?>
        get() = _logout

    companion object {
        private const val TAG = "GLOB_DEBUG USER_SERVICE"
        private const val USER_COLLECTION = "users"
    }

    suspend fun createUserDocument(uid: String, credentials: UserCredentials) = runCatching {

        val user = hashMapOf(
            "email" to credentials.email,
            "username" to credentials.username,
            "nickname" to credentials.username,
            "role" to "AUTHENTICATED",
            "user_id" to generateId(),
            "favorites" to emptyArray<Any>()
        )

        firebase.createDocumentWithId(USER_COLLECTION, user, uid)

    }.isSuccess

    fun fetchUserDocument(uid: String?) {
        Log.i(TAG, "Fetch user document: ${uid.toString()}")
        if (uid != null) {
            val docRef = firebase.getDocRef(USER_COLLECTION, uid)
            docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    _userData.value = null
                    _logout.value = true
                }
                if (snapshot != null && snapshot.exists()) {
                    if (isClient(snapshot.data)) {
                        Log.d(TAG, "User is client!! ;)")
                        _userData.value = snapshot.data!!.asUserDataResponse()
                        _logout.value = false
                    } else {
                        Log.w(TAG, "User is not client")
                        _userData.value = null
                        _logout.value = true
                    }
                } else {
                    Log.w(TAG, "Current data: null")
                    _userData.value = null
                    _logout.value = true
                }
            }
        } else {
            _userData.value = null
            _logout.value = null
        }
    }

    private fun isClient(data: Map<String, Any>?): Boolean {
        return data?.get("role") == "AUTHENTICATED"
    }

    // TODO move this function to core
    private fun generateId(): String {
        val firstPart = (Math.random() * 1000000000000000).toLong().toString(36)
        val secondPart = (Math.random() * 1000000000000000).toLong().toString(36)
        return firstPart.substring(0, 13) + secondPart.substring(0, 13)
    }

}