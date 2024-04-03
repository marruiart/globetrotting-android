package com.marina.ruiz.globetrotting.ui.main.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.dialog.FullScreenDialogFragment
import com.marina.ruiz.globetrotting.databinding.FragmentEditProfileBinding

interface EditProfileDialogFragmentListener {
    fun onAccept(data: String)
    fun onCancel()
}

class EditProfileDialogFragment(
    private val callback: EditProfileDialogFragmentListener
) : FullScreenDialogFragment(R.layout.fragment_edit_profile) {
    private val padding = 100
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var positiveBtn: Button
    private lateinit var neutralBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets(view)
        positiveBtn = binding.btnAcceptEditProfile
        neutralBtn = binding.btnCancelEditProfile
        initListeners()
    }

    private fun setWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + padding,
                systemBars.top + padding,
                systemBars.right + padding,
                systemBars.bottom + padding
            )
            insets
        }
    }

    private fun initListeners() {
        positiveBtn.setOnClickListener {
            callback.onAccept("Hola mundo")
        }
        neutralBtn.setOnClickListener {
            callback.onCancel()
        }
    }
}
