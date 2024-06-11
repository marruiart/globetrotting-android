package com.marina.ruiz.globetrotting.domain

import android.net.Uri
import android.util.Log
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.StorageFileListeners
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.ProfilePayload
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadAvatarUseCase @Inject constructor(
    private val authSvc: AuthService, private val userSvc: UserService
) {

    companion object {
        private const val TAG = "GLOB_DEBUG UPLOAD_AVATAR_USECASE"
    }

    operator fun invoke(
        avatar: Uri?, profile: ProfilePayload, clientName: String?, callback: StorageFileListeners
    ) {
        authSvc.uid.value?.let { uid ->
            Log.d(TAG, "URI: $avatar")
            userSvc.updateAvatar(uid, avatar, profile, clientName, callback)
        }
    }

}