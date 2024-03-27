package com.marina.ruiz.globetrotting.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.ui.auth.AuthViewModel

class ProfileFragment : Fragment() {
    private val authViewModel: AuthViewModel by viewModels()

    companion object {
        fun newInstance() = ProfileFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
        initObservers()
    }

    private fun initObservers() {
        val owner = viewLifecycleOwner

        authViewModel.navigateToHome.observe(owner) { isLogged ->
            if (!isLogged) {
                navigateToLogin()
            }
        }
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateToLogin() {
    }

}