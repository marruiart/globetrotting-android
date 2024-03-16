package com.marina.ruiz.globetrotting.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.marina.ruiz.globetrotting.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            createAccount.setOnClickListener { navigateToRegisterForm() }
            loginBtn.setOnClickListener { navigateHome() }
        }
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateToRegisterForm() =
        navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())

    private fun navigateHome() =
        navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())

}