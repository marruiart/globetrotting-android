package com.marina.ruiz.globetrotting.data.network.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.marina.ruiz.globetrotting.data.network.firebase.model.DocumentData
import com.marina.ruiz.globetrotting.data.network.firebase.model.asAgentResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.FavoritesPayload
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.ProfilePayload
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.AgentResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.UserDataResponse
import com.marina.ruiz.globetrotting.data.network.firebase.model.response.asUserDataResponse
import com.marina.ruiz.globetrotting.domain.SignUpListeners
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface StorageFileListeners {
    fun onUploadSuccess(downloadUri: Uri?, profile: ProfilePayload, clientName: String?)
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

    suspend fun createUserDocument(
        uid: String,
        username: String,
        email: String
    ) = runCatching {
        val user = hashMapOf(
            "email" to email,
            "username" to username,
            "nickname" to username,
            "role" to "AUTHENTICATED",
            "user_id" to uid,
            "favorites" to emptyList<Any>(),
            "createdAt" to Timestamp.now()
        )

        firebase.createDocumentWithId(USER_COLLECTION, user, uid)

    }.isSuccess

    suspend fun editUserDocument(uid: String, profile: ProfilePayload) {
        firebase.updateDocument(USER_COLLECTION, profile.asHashMap(), uid)
    }

    suspend fun editUserFavorites(uid: String, favorites: FavoritesPayload) {
        firebase.updateDocument(USER_COLLECTION, favorites.asHashMap(), uid)
    }

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
                Log.d(TAG, "Current agents: ${_agentsData.value.map { it.username }}")
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

    fun updateAvatar(
        uid: String,
        file: Uri?,
        profile: ProfilePayload,
        clientName: String?,
        callback: StorageFileListeners
    ) {
        if (file != null) {
            firebase.uploadFile(uid, file, profile, clientName, callback)
        } else {
            firebase.removeFile(uid, profile, clientName, callback)
        }
    }

}