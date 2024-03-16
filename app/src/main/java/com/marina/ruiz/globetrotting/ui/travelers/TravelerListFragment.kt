package com.marina.ruiz.globetrotting.ui.travelers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.marina.ruiz.globetrotting.databinding.FragmentTravelerListBinding
import com.marina.ruiz.globetrotting.ui.travelers.adapter.TravelerListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TravelerListFragment : Fragment() {
    private lateinit var binding: FragmentTravelerListBinding
    private val viewModel: TravelerListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelerListBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun bindView(adapter: TravelerListAdapter) {
        val rv = binding.travelersList
        rv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.travelers.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}