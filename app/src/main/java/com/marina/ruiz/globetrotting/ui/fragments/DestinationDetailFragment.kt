package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationDetailBinding
import com.marina.ruiz.globetrotting.ui.viewmodels.DestinationDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DestinationDetailFragment : Fragment() {
    private lateinit var binding: FragmentDestinationDetailBinding
    private val args: DestinationDetailFragmentArgs by navArgs()
    private val viewModel: DestinationDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDestinationDetailBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.topAppBar)
        fetchDescription()
        bindView(args.destination)
    }

    private fun bindView(destination: Destination) {
        binding.topAppBar.setNavigationOnClickListener {
            navigateBack()
        }
        binding.destinationName.text = destination.name
        if (destination.imageRef != null) {
            binding.destinationImg.setImageResource(destination.imageRef as Int)
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

    private fun fetchDescription() {
        if (args.destination.description.isEmpty()) {
            viewModel.updateDescription(args.destination)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.destination.collect { destination ->
                    bindView(destination)
                }
            }
        }
    }
}