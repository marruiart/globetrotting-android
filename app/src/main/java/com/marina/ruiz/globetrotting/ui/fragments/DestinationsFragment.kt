package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.databinding.FragmentBookingCreationFormBinding
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationsBinding
import com.marina.ruiz.globetrotting.ui.adapter.DestinationsListAdapter
import com.marina.ruiz.globetrotting.ui.adapter.TravelerListAdapter
import com.marina.ruiz.globetrotting.ui.viewmodels.DestinationsListViewModel
import com.marina.ruiz.globetrotting.ui.viewmodels.TravelerListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DestinationsFragment : Fragment() {
    private lateinit var binding: FragmentDestinationsBinding
    private val viewModel: DestinationsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDestinationsBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter = DestinationsListAdapter()
        val rv = binding.destinationsList
        rv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.destinations.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}
