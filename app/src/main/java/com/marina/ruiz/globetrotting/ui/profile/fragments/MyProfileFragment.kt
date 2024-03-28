package com.marina.ruiz.globetrotting.ui.profile.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.ui.auth.AuthViewModel
import com.marina.ruiz.globetrotting.ui.profile.ProfileViewModel

class MyProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()

    companion object {
        fun newInstance() = MyProfileFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObservers()
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    private fun initObservers() {
        val owner = viewLifecycleOwner
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }

}