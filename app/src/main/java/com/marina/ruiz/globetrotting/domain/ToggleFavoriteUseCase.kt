package com.marina.ruiz.globetrotting.domain

import android.util.Log
import com.marina.ruiz.globetrotting.data.local.LocalRepository
import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import com.marina.ruiz.globetrotting.data.network.firebase.UserService
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.FavoritesPayload
import com.marina.ruiz.globetrotting.data.repository.model.Favorite
import com.marina.ruiz.globetrotting.data.repository.model.asEntityList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToggleFavoriteUseCase @Inject constructor(
    private val authSvc: AuthService,
    private val userSvc: UserService
) {

    companion object {
        private const val TAG = "GLOB_DEBUG TOGGLE_FAVORITE_USECASE"
    }

    suspend operator fun invoke(favorites: MutableList<Favorite>) {
        authSvc.uid.value?.let { uid ->
            Log.d(TAG, "Changing favorites in firebase...")
            userSvc.editUserFavorites(uid, FavoritesPayload(favorites))
        }
    }

}