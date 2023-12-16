package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.marina.ruiz.globetrotting.databinding.FragmentBookingsBinding
import com.marina.ruiz.globetrotting.ui.adapter.BookingsListAdapter
import com.marina.ruiz.globetrotting.ui.viewmodels.BookingsListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingsFragment : Fragment() {
    private lateinit var binding: FragmentBookingsBinding
    private val viewModel: BookingsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingsBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adapter = BookingsListAdapter()
        val rv = binding.bookingsList
        rv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookings.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
}