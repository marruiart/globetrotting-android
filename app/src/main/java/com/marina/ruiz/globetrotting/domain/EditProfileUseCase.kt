package com.marina.ruiz.globetrotting.domain

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.StorageFileListeners
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.ProfilePayload
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditProfileUseCase @Inject constructor(
    private val authSvc: AuthService, private val userSvc: UserService
) : StorageFileListeners {
    private lateinit var _context: Context
    private lateinit var _profile: ProfilePayload
    private lateinit var _uid: String
    private val response: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    companion object {
        private const val TAG = "GLOB_DEBUG EDIT_PROFILE_USECASE"
    }

    suspend operator fun invoke(
        context: Context, profile: ProfilePayload, avatar: Uri?, removeImage: Boolean
    ): Boolean {
        authSvc.uid.value?.let { uid ->
            _context = context
            _profile = profile
            _uid = uid
            Log.d(TAG, "URI: $avatar")
            if (avatar == null && removeImage || avatar != null) {
                userSvc.updateAvatar(uid, avatar, this)
            } else {
                userSvc.editUserDocument(uid, _profile)
            }
            if (response.value == null) {
                response.collect { successOrNull ->
                    successOrNull?.let { success ->
                        if (success) {
                            userSvc.editUserDocument(uid, _profile)
                            response.value = false
                        }
                    }
                }
            }
        }
        return false
    }

    override fun onUploadSuccess(downloadUri: Uri?) {
        if (downloadUri == null) {
            _profile.avatar = null
        } else if (downloadUri.toString() != _profile.avatar) {
            _profile.avatar = downloadUri.toString()
            Log.d(TAG, "New URL: $downloadUri")
            Toast.makeText(_context, "Foto actualizada", Toast.LENGTH_SHORT).show()
        }
        response.value = true
    }

    override fun onUploadFailed(exception: Exception) {
        Log.e("GLOB_DEBUG EDIT_PROFILE_USECASE", exception.message ?: "Upload failed")
        Toast.makeText(_context, "Error al actualizar la foto de perfil", Toast.LENGTH_SHORT).show()
        response.value = false
    }

}