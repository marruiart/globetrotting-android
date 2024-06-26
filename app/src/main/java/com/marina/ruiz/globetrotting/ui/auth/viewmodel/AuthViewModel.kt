package com.marina.ruiz.globetrotting.ui.auth.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.core.extension.isValidEmail
import com.marina.ruiz.globetrotting.core.extension.isValidPassword
import com.marina.ruiz.globetrotting.data.network.firebase.model.result.LoginResult
import com.marina.ruiz.globetrotting.data.repository.GlobetrottingRepository
import com.marina.ruiz.globetrotting.data.repository.model.User
import com.marina.ruiz.globetrotting.domain.ForgotPasswordListeners
import com.marina.ruiz.globetrotting.domain.ForgotPasswordUseCase
import com.marina.ruiz.globetrotting.domain.LoginUseCase
import com.marina.ruiz.globetrotting.domain.LogoutUseCase
import com.marina.ruiz.globetrotting.domain.SignUpListeners
import com.marina.ruiz.globetrotting.domain.SignUpUseCase
import com.marina.ruiz.globetrotting.ui.auth.LoginViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val createAccount: SignUpUseCase,
    private val repository: GlobetrottingRepository
) : ViewModel() {

    private companion object {
        private const val TAG = "GLOB_DEBUG AUTH_VM"

        const val MIN_PASSWORD_LENGTH = 6
    }

    // LIVEDATA VARIABLES
    private val _allowAccess = MutableLiveData<Boolean>(false)
    val allowAccess: LiveData<Boolean> = _allowAccess

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState

    private val _showErrorDialog = MutableLiveData<Boolean>()
    val showErrorDialog: LiveData<Boolean> = _showErrorDialog

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    /**
     * Initializes the ViewModel, setting up initial states and collecting user data.
     */
    init {
        _allowAccess.value = repository.checkAccess()
        Log.d(TAG, "First access: ${_allowAccess.value}")
        viewModelScope.launch {
            repository.localUser.distinctUntilChangedBy { user -> user?.uid }.collect { user ->
                _user.value = user
                Log.w(TAG, "Collected user from room: ${user?.uid}")
                if (user != null) {
                    Log.d(TAG, "Allow access")
                    _allowAccess.value = true
                }
            }
        }
        viewModelScope.launch {
            repository.logout.collect { value ->
                value?.let { logout ->
                    if (logout) {
                        Log.d(TAG, "Login out...")
                        _showErrorDialog.value = true
                        onLogout()
                    } else {
                        Log.d(TAG, "Login in...")
                        _allowAccess.value = true
                    }
                }
            }
        }
    }

    /**
     * Updates the view state based on changes in the email and password fields.
     *
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     */
    private fun onFieldsChanged(email: String, password: String) {
        _viewState.value = LoginViewState(
            isValidEmail = email.isValidEmail(), isValidPassword = password.isValidPassword()
        )
    }

    /**
     * Handles the login operation.
     *
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     */
    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _viewState.value = LoginViewState(isLoading = true)
            val result = loginUseCase(email, password)
            when (result) {
                is LoginResult.Error -> {
                    _showErrorDialog.value = true
                    resetData()
                }

                is LoginResult.Success -> {
                    Log.d(TAG, "Login Success")
                    repository.collectUserData()
                }
            }
            _viewState.value = LoginViewState(isLoading = false)
        }
    }

    private fun resetData() {
        _user.value = null
        _allowAccess.value = false
    }

    // PUBLIC FUNCTIONS

    fun forgotPassword(email: String,callback: ForgotPasswordListeners) {
        forgotPasswordUseCase(email, callback)
    }


    /**
     * Handles the login operation.
     *
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     */
    fun onLogin(email: String, password: String) {
        if (email.isValidEmail() && password.isValidPassword()) {
            Log.d(TAG, "Init login...")
            loginUser(email, password)
        } else {
            onFieldsChanged(email, password)
        }
    }


    /**
     * Handles the logout operation.
     */
    fun onLogout() {
        logoutUseCase()
        viewModelScope.launch {
            Log.e(TAG, "Erasing database...")
            repository.eraseDatabase()
        }
        resetData()
    }

    /**
     * Handles the account creation process.
     *
     * @param username The username entered by the user.
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     * @param callback The callback to handle the sign up response.
     */
    fun onCreateAccount(
        username: String,
        email: String,
        password: String,
        callback: SignUpListeners
    ) {
        viewModelScope.launch {
            createAccount(username, email, password, callback)
        }
    }
}