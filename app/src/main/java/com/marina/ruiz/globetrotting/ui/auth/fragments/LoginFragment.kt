package com.marina.ruiz.globetrotting.ui.auth.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.extension.dismissKeyboard
import com.marina.ruiz.globetrotting.databinding.FragmentLoginBinding
import com.marina.ruiz.globetrotting.ui.auth.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
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
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                it.dismissKeyboard()
                val email = binding.etLoginEmail.text.toString()
                val password = binding.etLoginPassword.text.toString()
                authVM.onLogin(email, password)
            }
            tvCreateAccount.setOnClickListener { navigateToRegisterForm() }
        }
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

}