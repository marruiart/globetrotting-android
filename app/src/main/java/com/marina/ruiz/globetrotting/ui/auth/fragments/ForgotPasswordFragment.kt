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
import com.marina.ruiz.globetrotting.core.extension.isValidEmail
import com.marina.ruiz.globetrotting.databinding.FragmentForgotPasswordBinding
import com.marina.ruiz.globetrotting.domain.ForgotPasswordListeners
import com.marina.ruiz.globetrotting.ui.auth.viewmodel.AuthViewModel

class ForgotPasswordFragment : Fragment(), ForgotPasswordListeners {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var etEmail: TextInputEditText
    private lateinit var btnRemindPassword: Button
    private var boxStrokeColor = Color.BLACK
    private val authVM: AuthViewModel by activityViewModels()

    companion object {
        private const val TAG = "GLOBAL_DEBUG SIGN_UP_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.mtSignupToolbar)
        initUI()
    }


    private fun initUI() {
        boxStrokeColor = binding.tilForgotEmail.boxStrokeColor
        initListeners()
    }

    private fun initListeners() {
        binding.mtSignupToolbar.setNavigationOnClickListener { navigateBack() }

        etEmail = binding.etForgotEmail
        btnRemindPassword = binding.btnRemindPassword

        validateEmail()
        toggleActivationSignUpButton()
    }

    /**
     * Validates the email input field and updates the UI accordingly.
     */
    private fun validateEmail() {
        etEmail.doOnTextChanged { text, _, _, _ ->
            val valid = text.toString().isValidEmail()
            Log.d(TAG, "EMAIL: $valid")
            btnRemindPassword.isEnabled = valid
            if (!text.isNullOrEmpty() && !valid) {
                binding.tilForgotEmail.boxStrokeColor = Color.RED
            } else {
                binding.tilForgotEmail.boxStrokeColor = boxStrokeColor
            }
        }
    }

    /**
     * Toggles the activation state of the "Remind Password" button and sets its click listener.
     */
    private fun toggleActivationSignUpButton() {
        btnRemindPassword.setOnClickListener {
            val email = etEmail.text.toString()

            if (email.isValidEmail()) {
                authVM.forgotPassword(email, this)
            } else {
                showErrorDialog(requireContext().getString(R.string.login_error_response_error_title))
            }
        }
    }

    /**
     * Displays an error dialog with the specified message.
     *
     * @param message The message to display in the dialog
     */
    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext()).setTitle(requireContext().getString(R.string.login_error_response_error_title))
            .setMessage(message)
            .setNeutralButton(requireContext().getString(R.string.login_error_response_accept_btn)) { dialog, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL) dialog.dismiss()
            }.show()
    }


    private fun navigateBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onRemindPasswordSuccess(email: String) {
        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.forgot_password_sent, email),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRemindPasswordFailure(exception: Exception) {
        Log.e(TAG, exception.toString())
        var message = requireContext().getString(R.string.forgot_password_error_response)
        showErrorDialog(message)
    }
}