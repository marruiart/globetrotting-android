package com.marina.ruiz.globetrotting.ui.auth.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marina.ruiz.globetrotting.core.SecureSharedPrefs
import com.marina.ruiz.globetrotting.core.extension.dismissKeyboard
import com.marina.ruiz.globetrotting.databinding.FragmentLoginBinding
import com.marina.ruiz.globetrotting.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by activityViewModels()
    private var observer = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        Log.d("FRAGMENT", "onCreateView")
/*        lifecycleScope.launch {
            val role = viewModel.loginUseCase.getUserRole()
            if (role != null) {
                viewModel.allowAccess()
            }
        }*/
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
                viewModel.onLogin(email, password)
            }
        }
    }

    private fun initObservers() {
        val owner = viewLifecycleOwner
        with(viewModel) {
            userCredentials.observe(owner) { credentials ->
                val email = credentials.email
                val passwd = credentials.password
                val sharedPreferences = SecureSharedPrefs.getSharedPreferences(requireContext())
                val editor = sharedPreferences.edit()
                editor.putString("email", email)
                editor.putString("passwd", passwd)
                editor.apply()
                Log.d("FRAGMENT", "initObservers")
            }

            navigateToHome.observeForever { navigate ->
                if (navigate) {
                    navigateHome()
                }
            }
            showErrorDialog.observe(owner) { showError ->
                if (showError) {
                    showErrorDialog("No es posible la autenticación")
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setNeutralButton("Aceptar") { dialog, which ->
                if (which == DialogInterface.BUTTON_NEUTRAL)
                    dialog.dismiss()
            }.show()
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateToRegisterForm() =
        navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())

    private fun navigateHome() {
        navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
        requireActivity().finish()
    }

}