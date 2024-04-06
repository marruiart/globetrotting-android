package com.marina.ruiz.globetrotting.ui.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.domain.EditProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "GLOB_DEBUG PROFILE_VM"
    }

    fun editProfile(data: Profile) {
        viewModelScope.launch {
            editProfileUseCase(data)
            Log.d(TAG, "Accept " + data)
        }
    }
}