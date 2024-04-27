package com.marina.ruiz.globetrotting.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {

    companion object {
        private const val TAG = "GLOB_DEBUG MAIN_VM"
    }

    // LIVEDATA VARIABLES
    private val _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> = _logout

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        viewModelScope.launch {
            repository.collectUserData()
        }
        viewModelScope.launch {
            repository.collectData()
        }
        viewModelScope.launch {
            repository.localUser.collect { user ->
                user?.let {
                    _user.value = it
                }
                if (user == null) {
                    logout()
                }
            }
        }
        Log.d(TAG, "MainViewModel")
    }

    private fun logout() {
        _logout.value = true
    }

    fun eraseDatabase() {
        viewModelScope.launch {
            repository.eraseDatabase()
        }
    }

}