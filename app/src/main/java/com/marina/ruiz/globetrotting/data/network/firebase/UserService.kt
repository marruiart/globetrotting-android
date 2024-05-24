package com.marina.ruiz.globetrotting.data.network.firebase

import android.net.Uri
import android.util.Log
import com.marina.ruiz.globetrotting.data.network.firebase.model.DocumentData
import com.marina.ruiz.globetrotting.data.network.firebase.model.asAgentResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.FavoritesPayload
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.Payload
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.ProfilePayload
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.AgentResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.UserDataResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.asUserDataResponse
import com.marina.ruiz.globetrotting.ui.auth.model.UserCredentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface StorageFileListeners {
    fun onUploadSuccess(downloadUri: Uri?)
    fun onUploadFailed(exception: Exception)
}

@Singleton
class UserService @Inject constructor(private val firebase: FirebaseService) {

    private val _userData = MutableStateFlow<UserDataResponse?>(null)
    val userResponse: StateFlow<UserDataResponse?>
        get() = _userData

    private val _agentsData = MutableStateFlow<List<AgentResponse>>(emptyList())
    val agentsResponse: StateFlow<List<AgentResponse>>
        get() = _agentsData

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
            "user_id" to Payload.generateId(),
            "favorites" to emptyArray<Any>()
        )

        firebase.createDocumentWithId(USER_COLLECTION, user, uid)

    }.isSuccess

    suspend fun editUserDocument(uid: String, profile: ProfilePayload) = runCatching {
        firebase.updateDocument(USER_COLLECTION, profile.asHashMap(), uid)
    }.isSuccess

    suspend fun editUserFavorites(uid: String, favorites: FavoritesPayload) = runCatching {
        firebase.updateDocument(USER_COLLECTION, favorites.asHashMap(), uid)
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
                } else if (snapshot != null && snapshot.exists()) {
                    checkUserRole(snapshot.data)
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

    fun fetchAgents() {
        Log.i(TAG, "Fetch agents")
        firebase.getCollectionRef(USER_COLLECTION).whereNotEqualTo("role", "AUTHENTICATED")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                _agentsData.value = snapshot!!.map { doc ->
                    (doc.data as DocumentData).asAgentResponse()
                }
                Log.d(TAG, "Current agents: ${_agentsData.value}")
            }
    }

    private fun checkUserRole(data: Map<String, Any>?) {
        if (isClient(data)) {
            Log.d(TAG, "User is client!! ;)")
            _userData.value = data!!.asUserDataResponse()
            _logout.value = false
        } else {
            Log.w(TAG, "User is not client")
            _userData.value = null
            _logout.value = true
        }
    }

    private fun isClient(data: Map<String, Any>?): Boolean {
        return data?.get("role") == "AUTHENTICATED"
    }

    fun updateAvatar(uid: String, file: Uri?, callback: StorageFileListeners) {
        if (file != null) {
            firebase.uploadFile(uid, file, callback)
        } else {
            firebase.removeFile(uid, callback)
        }
    }

}