package com.marina.ruiz.globetrotting.domain

import android.net.Uri
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.StorageFileListeners
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.ProfilePayload
import com.marina.ruiz.globetrotting.ui.main.profile.model.ProfileForm
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val authSvc: AuthService,
    private val userSvc: UserService
) {

    suspend operator fun invoke(profile: ProfilePayload, avatar: Uri?, callback: StorageFileListeners): Boolean {
        authSvc.uid.value?.let { uid ->
            userSvc.updateAvatar(uid, avatar, callback)
            return userSvc.editUserDocument(uid, profile)
        }
        return false
    }

}