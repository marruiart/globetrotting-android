package com.marina.ruiz.globetrotting.domain

import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.ui.main.profile.model.Profile
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val authSvc: AuthService,
    private val userSvc: UserService
) {

    suspend operator fun invoke(data: Profile): Boolean {
        authSvc.uid.value?.let { uid ->
            return userSvc.editUserDocument(uid, data)
        }
        return false
    }

}