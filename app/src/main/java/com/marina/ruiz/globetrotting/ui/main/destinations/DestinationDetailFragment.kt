package com.marina.ruiz.globetrotting.ui.main.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DestinationDetailFragment : Fragment() {
    private lateinit var binding: FragmentDestinationDetailBinding
    private val args: DestinationDetailFragmentArgs by navArgs()
    private val destinationsVM: DestinationsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDestinationDetailBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.mtDestinationDetailToolbar)
       // fetchDescription()
        bindView(args.destination)
    }

    private fun bindView(destination: Destination) {
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

/*    private fun fetchDescription() {
        if (args.destination.description.isEmpty()) {
            destinationsVM.updateDescription(args.destination, requireActivity().lifecycleScope)
            viewLifecycleOwner.lifecycleScope.launch {
                destinationsVM.destination.collect { destination ->
                    bindView(destination)
                }
            }
        }
    }*/
}