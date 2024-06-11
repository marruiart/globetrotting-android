package com.marina.ruiz.globetrotting.domain

import android.util.Log
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.BookingsService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.ProfilePayload
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditProfileUseCase @Inject constructor(
    private val authSvc: AuthService,
    private val userSvc: UserService,
    private val bookingSvc: BookingsService
) {

    companion object {
        private const val TAG = "GLOB_DEBUG EDIT_PROFILE_USECASE"
    }

    suspend operator fun invoke(
        profile: ProfilePayload, clientName: String?
    ) {
        authSvc.uid.value?.let { uid ->
            updateUserProfile(uid, profile, clientName)
            updateBookings(clientName)
        }
    }

    private suspend fun updateUserProfile(
        uid: String, profile: ProfilePayload, clientName: String?
    ) {
        Log.i(TAG, "Updating profile...")
        userSvc.editUserDocument(uid, profile)
        updateBookings(clientName)
    }

    private fun updateBookings(clientName: String?) {
        if (clientName != null) {
            Log.d(TAG, "New name: $clientName")
            Log.i(TAG, "Updating bookings...")
            bookingSvc.updateBookings(clientName)
        }
    }

}