package com.marina.ruiz.globetrotting.ui.auth.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.extension.comparePassword
import com.marina.ruiz.globetrotting.core.extension.dismissKeyboard
import com.marina.ruiz.globetrotting.core.extension.isValidEmail
import com.marina.ruiz.globetrotting.core.extension.isValidPassword
import com.marina.ruiz.globetrotting.core.extension.validateEmailAndPassword
import com.marina.ruiz.globetrotting.databinding.FragmentLoginBinding
import com.marina.ruiz.globetrotting.ui.auth.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLoginAccept: Button
    private var boxStrokeColor = Color.BLACK
    private val authVM: AuthViewModel by activityViewModels()

    companion object {
        private const val TAG = "GLOB_DEBUG LOGIN_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        boxStrokeColor = binding.tilFormEmail.boxStrokeColor
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        with(binding) {
            etEmail = etLoginEmail
            etPassword = etLoginPassword
            btnLoginAccept = btnLogin
            btnLogin.setOnClickListener {
                it.dismissKeyboard()
                val email = binding.etLoginEmail.text.toString()
                val password = binding.etLoginPassword.text.toString()
                authVM.onLogin(email, password)
            }
            tvCreateAccount.setOnClickListener { navigateToRegisterForm() }
            tvForgotPassword.setOnClickListener { navigateToForgotPassword() }
        }
        validateEmail()
        validatePassword()
    }

    private fun initObservers() {
        val owner = viewLifecycleOwner
        with(authVM) {
            showErrorDialog.observe(owner) { showError ->
                if (showError) {
                    showErrorDialog(requireContext().getString(R.string.login_error_response))
                }
            }
        }
    }

    /**
     * Validates the email input field and updates the UI accordingly.
     */
    private fun validateEmail() {
        etEmail.doOnTextChanged { text, _, _, _ ->
            val valid = text.toString().isValidEmail()
            Log.d(TAG, "EMAIL: $valid")
            btnLoginAccept.isEnabled = valid
            if (!text.isNullOrEmpty() && !valid) {
                binding.tilFormEmail.boxStrokeColor = Color.RED
            } else {
                binding.tilFormEmail.boxStrokeColor = boxStrokeColor
            }
        }
    }

    /**
     * Validates the password input fields and updates the UI accordingly.
     */
    private fun validatePassword() {
        etPassword.doOnTextChanged { text, _, _, _ ->
            val valid = text.toString().isValidPassword()
            Log.d(TAG, "PASSWORD: $valid")
            btnLoginAccept.isEnabled = valid && etEmail.text.toString().isValidEmail()
            if (!text.isNullOrEmpty() && !valid) {
                binding.tilFormPassword.boxStrokeColor = Color.RED
            } else {
                binding.tilFormPassword.boxStrokeColor = boxStrokeColor
            }
        }
    }


    /**
     * Displays an error dialog with the specified message.
     *
     * @param message The message to be shown in the error dialog.
     */
    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext()).setTitle(requireContext().getString(R.string.login_error_response_error_title))
            .setMessage(message)
            .setNeutralButton(requireContext().getString(R.string.login_error_response_accept_btn)) { dialog, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL) dialog.dismiss()
            }.show()
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateToRegisterForm() =
        navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())

    private fun navigateToForgotPassword() =
        navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
}