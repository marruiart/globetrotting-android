package com.marina.ruiz.globetrotting.ui.auth.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.extension.comparePassword
import com.marina.ruiz.globetrotting.core.extension.isValidEmail
import com.marina.ruiz.globetrotting.core.extension.isValidPassword
import com.marina.ruiz.globetrotting.core.extension.validateEmailAndPassword
import com.marina.ruiz.globetrotting.databinding.FragmentRegisterBinding
import com.marina.ruiz.globetrotting.domain.SignUpListeners
import com.marina.ruiz.globetrotting.ui.auth.viewmodel.AuthViewModel

class SignUpFragment : Fragment(), SignUpListeners {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var etUsername: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etPasswordRepeat: TextInputEditText
    private lateinit var btnSignupRegister: Button
    private var boxStrokeColor = Color.BLACK
    private val authVM: AuthViewModel by activityViewModels()

    companion object {
        private const val TAG = "GLOBAL_DEBUG SIGN_UP_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.mtSignupToolbar)
        initUI()
    }

    private fun initUI() {
        boxStrokeColor = binding.tilCreateFormEmail.boxStrokeColor
        initListeners()
    }

    private fun initListeners() {
        binding.mtSignupToolbar.setNavigationOnClickListener { navigateBack() }

        etUsername = binding.etCreateFormUsername
        etEmail = binding.etCreateFormEmail
        etPassword = binding.etCreateFormPassword
        etPasswordRepeat = binding.etCreateFormRepeatPassword
        btnSignupRegister = binding.btnCreateAccount

        validateEmail()
        validatePassword()
        toggleActivationSignUpButton()
    }

    private fun validateEmail() {
        etEmail.doOnTextChanged { text, _, _, _ ->
            val valid = text.toString().isValidEmail()
            Log.d(TAG, "EMAIL: $valid")
            btnSignupRegister.isEnabled = valid
            if (!text.isNullOrEmpty() && !valid) {
                binding.tilCreateFormEmail.boxStrokeColor = Color.RED
            } else {
                binding.tilCreateFormEmail.boxStrokeColor = boxStrokeColor
            }
        }
    }

    private fun toggleActivationSignUpButton() {
        btnSignupRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val passwordRepeat = etPasswordRepeat.text.toString()

            if (validateEmailAndPassword(
                    email, password
                ) && password.comparePassword(passwordRepeat)
            ) {
                authVM.onCreateAccount(username, email, password, this)
            } else {
                showErrorDialog(requireContext().getString(R.string.login_error_response_error_title))
            }
        }
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext()).setTitle(requireContext().getString(R.string.login_error_response_error_title))
            .setMessage(message)
            .setNeutralButton(requireContext().getString(R.string.login_error_response_accept_btn)) { dialog, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL) dialog.dismiss()
            }.show()
    }

    private fun validatePassword() {
        etPassword.doOnTextChanged { text, _, _, _ ->
            val valid = text.toString().isValidPassword() && etPassword.text.toString()
                .comparePassword(etPasswordRepeat.text.toString())
            Log.d(TAG, "PASSWORD: $valid")
            btnSignupRegister.isEnabled = valid && etEmail.text.toString().isValidEmail()
            if (!text.isNullOrEmpty() && !valid) {
                binding.tilCreateFormPassword.boxStrokeColor = Color.RED
            } else {
                binding.tilCreateFormPassword.boxStrokeColor = boxStrokeColor
            }
        }
        etPasswordRepeat.doOnTextChanged { text, _, _, _ ->
            val valid = text.toString().isValidPassword() && etPassword.text.toString()
                .comparePassword(etPasswordRepeat.text.toString())
            Log.d(TAG, "PASSWORD: $valid")
            btnSignupRegister.isEnabled = valid && etEmail.text.toString().isValidEmail()
            if (!text.isNullOrEmpty() && !valid) {
                binding.tilCreateFormRepeatPassword.boxStrokeColor = Color.RED
            } else {
                binding.tilCreateFormRepeatPassword.boxStrokeColor = boxStrokeColor
            }
        }
    }

    private fun navigateBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onSignUpSuccess(username: String) {
        Toast.makeText(requireContext(), requireContext().getString(R.string.welcome, username), Toast.LENGTH_SHORT).show()
    }

    override fun onSignUpFailure(exception: Exception) {
        var message = requireContext().getString(R.string.signup_error_response)
        if (exception.message?.contains("already in use") == true) {
            message = requireContext().getString(R.string.signup_error_response_collision)
        } else if (exception.message?.contains("document") == true) {
            message = requireContext().getString(R.string.signup_error_response_collision)
        }
        showErrorDialog(message)
    }

}