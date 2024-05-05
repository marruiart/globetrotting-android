package com.marina.ruiz.globetrotting.ui.main.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.appbar.MaterialToolbar
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.dialog.FullScreenDialogFragment
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.DialogDestinationDetailBinding
import dagger.hilt.android.AndroidEntryPoint

interface DestinationDetailDialogListener {
    fun onCancelDetails()
}

@AndroidEntryPoint
class DestinationDetailDialog(
    private val callback: DestinationDetailDialogListener, private val destination: Destination
) : FullScreenDialogFragment(R.layout.dialog_destination_detail) {
    private lateinit var binding: DialogDestinationDetailBinding
    private val padding = 16
    private val destinationsVM: DestinationsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overrideOnBackPressed()
        if (destination.description.isEmpty()) {
            destinationsVM.fetchDescription(destination)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogDestinationDetailBinding.inflate(inflater, container, false)
        bindView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets()
    }

    private fun setWindowInsets() {
        val view =
            requireActivity().findViewById<MaterialToolbar>(R.id.mt_destination_detail_toolbar)
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

    private fun overrideOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    callback.onCancelDetails()
                    isEnabled = false
                    super.handleOnBackCancelled()
                }
            })
    }

    private fun bindView() {
        binding.mtDestinationDetailToolbar.setNavigationOnClickListener {
            navigateBack()
        }
        binding.destinationName.text = destination.name
        if (destination.imageRef != null) {
            binding.destinationImg.setImageResource(destination.imageRef)
        }
        binding.destinationDescription.text = destination.description
        if (binding.destinationDescription.text.isEmpty()) {
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
        }
    }

    private fun navigateBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}