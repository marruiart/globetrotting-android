package com.marina.ruiz.globetrotting.ui.main.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.dialog.FullScreenDialogFragment
import com.marina.ruiz.globetrotting.databinding.FragmentEditProfileBinding
import com.marina.ruiz.globetrotting.ui.main.profile.model.Profile
import com.marina.ruiz.globetrotting.ui.main.profile.model.ProfileForm

interface EditProfileDialogFragmentListener {
    fun onAccept(data: Profile)
    fun onCancel()
}

class EditProfileDialogFragment(
    private val callback: EditProfileDialogFragmentListener,
    private val profile: Profile
) : FullScreenDialogFragment(R.layout.fragment_edit_profile) {
    private val padding = 100
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var form: ProfileForm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets(view)
        initForm()
        bindView()
        initListeners()
    }

    private fun initForm() {
        form = ProfileForm(
            tilName = binding.tilFormName,
            tilSurname = binding.tilFormSurname,
            tilNickname = binding.tilFormNickname,
            positiveBtn = binding.btnAcceptEditProfile,
            neutralBtn = binding.btnCancelEditProfile
        )
    }

    private fun bindView() {
        form.tilName.setText(profile.name)
        form.tilSurname.setText(profile.surname)
        form.tilNickname.setText(profile.nickname)
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
        form.positiveBtn.setOnClickListener {
            val profile = Profile(
                name = form.tilName.text.toString(),
                surname = form.tilSurname.text.toString(),
                nickname = form.tilNickname.text.toString()
            )
            callback.onAccept(profile)
        }
        form.neutralBtn.setOnClickListener {
            callback.onCancel()
        }
    }
}
