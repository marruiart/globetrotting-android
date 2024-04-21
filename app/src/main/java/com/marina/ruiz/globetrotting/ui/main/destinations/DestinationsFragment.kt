package com.marina.ruiz.globetrotting.ui.main.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationsBinding
import com.marina.ruiz.globetrotting.ui.main.destinations.adapter.DestinationsListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DestinationsFragment : Fragment() {
    private lateinit var binding: FragmentDestinationsBinding
    private lateinit var adapter: DestinationsListAdapter
    private val destinationsVM: DestinationsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDestinationsBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        destinationsVM.bindView(adapter)
    }

    private fun initAdapter() {
        adapter = DestinationsListAdapter(::onShowDetail, ::onBookNow)
        val rv = binding.rvDestinationsList
        rv.adapter = adapter
    }

    private fun onShowDetail(destination: Destination, view: View) {
        val action =
            DestinationsFragmentDirections.actionDestinationsFragmentToDestinationDetailFragment(
                destination
            )
        view.findNavController().navigate(action)
    }

    private fun onBookNow(destination: Destination, view: View) {
        // TODO
    }

    private fun initObservers() {
        val owner = viewLifecycleOwner
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateToLogin() {
    }
}
