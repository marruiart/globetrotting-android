package com.marina.ruiz.globetrotting.ui.main.profile.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.marina.ruiz.globetrotting.data.repository.model.User
import com.marina.ruiz.globetrotting.databinding.FragmentMyProfileBinding

class MyProfileFragment : Fragment() {
    private lateinit var binding: FragmentMyProfileBinding
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val intent = requireActivity().intent
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        } else {
            intent.getParcelableExtra("user")
        }
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        user?.let {
            with(binding) {
                tvName.text = "${it.name} ${it.surname}"
                tvNickname.text = it.nickname
                tvUsername.text = it.username
                tvEmail.text = it.email
            }
        }
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        with(binding) {
            btnEdit.setOnClickListener {
                navigate(MyProfileFragmentDirections.actionMyProfileFragmentToEditProfileFragment())
            }
        }
    }

    private fun initObservers() {

    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }


}