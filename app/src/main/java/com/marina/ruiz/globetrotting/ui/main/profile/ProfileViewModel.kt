package com.marina.ruiz.globetrotting.ui.main.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.User
import com.marina.ruiz.globetrotting.domain.EditProfileUseCase
import com.marina.ruiz.globetrotting.ui.main.profile.model.ProfileForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    private val repository: GlobetrottingRepository
) : ViewModel() {

    private companion object {
        private const val TAG = "GLOB_DEBUG PROFILE_VM"
    }

    // LIVEDATA VARIABLES
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        viewModelScope.launch {
            repository.collectUserData()
        }
        viewModelScope.launch {
            repository.localUser.collect { user ->
                user?.let {
                    _user.value = it
                }
            }
        }
    }

    fun editProfile(context: Context, profile: ProfileForm, avatar: Uri?, removeImage: Boolean) {
        viewModelScope.launch {
            editProfileUseCase(context, profile.toProfilePayload(), avatar, removeImage)
            Log.d(TAG, "Accept $profile")
        }
    }
}