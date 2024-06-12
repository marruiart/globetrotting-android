package com.marina.ruiz.globetrotting.ui.main.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.network.firebase.StorageFileListeners
import com.marina.ruiz.globetrotting.data.network.firebase.model.payload.ProfilePayload
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.User
import com.marina.ruiz.globetrotting.domain.EditProfileUseCase
import com.marina.ruiz.globetrotting.domain.UploadAvatarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
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
        repository.collectLocalUser { user ->
            user?.let {
                _user.postValue(it)
            }
        }
    }

    /**
     * Uploads the user's avatar.
     *
     * @param avatar The URI of the avatar image.
     * @param profile The profile payload containing user information.
     * @param clientName The client name associated with the user.
     * @param callback The callback to handle upload events.
     */
    fun uploadAvatar(
        avatar: Uri?, profile: ProfilePayload, clientName: String?, callback: StorageFileListeners
    ) {
        uploadAvatarUseCase(avatar, profile, clientName, callback)
    }

    /**
     * Edits the user's profile.
     *
     * @param profile The profile payload containing updated user information.
     * @param clientName The client name associated with the user.
     */
    fun editProfile(profile: ProfilePayload, clientName: String?) {
        viewModelScope.launch {
            editProfileUseCase(profile, clientName)
        }
    }
}