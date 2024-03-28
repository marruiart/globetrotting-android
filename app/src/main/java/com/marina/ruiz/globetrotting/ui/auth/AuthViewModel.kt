package com.marina.ruiz.globetrotting.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marina.ruiz.globetrotting.data.network.firebase.model.LoginResult
import com.marina.ruiz.globetrotting.domain.LoginUseCase
import com.marina.ruiz.globetrotting.domain.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val loginUseCase: LoginUseCase,
    val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState>
        get() = _viewState

    private var _userCredentials = MutableLiveData(UserCredentials())
    val userCredentials: LiveData<UserCredentials>
        get() = _userCredentials


    private val _showErrorDialog = MutableLiveData<Boolean>()
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog


    private fun onFieldsChanged(email: String, password: String) {
        _viewState.value = LoginViewState(
            isValidEmail = isValidEmail(email), isValidPassword = isValidPassword(password)
        )
    }

    fun onLogin(email: String, password: String, navigate: Boolean = true) {
        if (isValidEmail(email) && isValidPassword(password)) {
            loginUser(email, password, navigate)
        } else {
            onFieldsChanged(email, password)
        }
    }

    fun onLogout() {
        logoutUseCase()
        _userCredentials.value = UserCredentials(email = "", password = "")
        _navigateToHome.value = false
    }

    fun allowAccess() {
        _navigateToHome.value = loginUseCase.isLogged()
    }

    private fun loginUser(email: String, password: String, navigate: Boolean = true) {
        viewModelScope.launch {
            _viewState.value = LoginViewState(isLoading = true)
            val result = loginUseCase(email, password)

            when (result) {
                LoginResult.Error -> _showErrorDialog.value = true

                is LoginResult.Success -> {
                    val role = loginUseCase.getUserRole()
                    if (role == "AUTHENTICATED") {
                        _userCredentials.value = UserCredentials(email = email, password = password)
                        if (navigate) {
                            _navigateToHome.value = true
                        }
                    } else {
                        onLogout()
                        _showErrorDialog.value = true
                    }
                }
            }
            _viewState.value = LoginViewState(isLoading = false)
        }
    }

    private fun isValidEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidPassword(password: String): Boolean =
        password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()
}