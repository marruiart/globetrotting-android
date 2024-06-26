package com.marina.ruiz.globetrotting.ui.main

import android.app.Activity
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GlobetrottingRepository
) : ViewModel() {
    var actionBarSize: Int = 0

    companion object {
        private const val TAG = "GLOB_DEBUG MAIN_VM"
    }

    // LIVEDATA VARIABLES
    private val _logout = MutableLiveData(false)
    val logout: LiveData<Boolean> = _logout

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    var registering = false


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
                    _user.postValue(it)
                }
                if (user == null && !registering) {
                    Log.d(TAG, "Null user login out...")
                    logout()
                }
                if (registering) {
                    registering = false
                }
            }
        }
        Log.d(TAG, "MainViewModel")
    }

    /**
     * Sets the _logout variable to true to activate the logout.
     */
    private fun logout() {
        _logout.value = true
    }

    /**
     * Erases the local database.
     */
    fun eraseDatabase() {
        viewModelScope.launch {
            repository.eraseDatabase()
        }
    }

    /**
     * Sets the top margin for the action bar size.
     * @param activity The activity instance.
     * @param remove The amount to remove.
     */
    fun setActionBarSizeMargin(activity: Activity, remove: Int = -1) {
        val fragmentContainerView =
            activity.findViewById<FragmentContainerView>(R.id.fragment_main_area)
        val params = fragmentContainerView.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = remove * actionBarSize
        fragmentContainerView.layoutParams = params
    }

}