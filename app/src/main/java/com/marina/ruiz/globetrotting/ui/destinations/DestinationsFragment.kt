package com.marina.ruiz.globetrotting.ui.destinations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationsBinding
import com.marina.ruiz.globetrotting.ui.auth.AuthViewModel
import com.marina.ruiz.globetrotting.ui.destinations.adapter.DestinationsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DestinationsFragment : Fragment() {
    private lateinit var binding: FragmentDestinationsBinding
    private val viewModel: DestinationsListViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

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
        var adapter = DestinationsListAdapter(::onShowDetail, ::onBookNow)
        bindView(adapter)
    }

    private fun bindView(adapter: DestinationsListAdapter) {
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

    private fun onShowDetail(destination: Destination, view: View) {
        val action =
            DestinationsFragmentDirections.actionDestinationsFragmentToDestinationDetailFragment(
                destination
            )
        view.findNavController().navigate(action)
    }

    private fun onBookNow(destination: Destination, view: View) {
        val action =
            DestinationsFragmentDirections.actionDestinationsFragmentToBookingCreationFormFragment(
                destination
            )
        view.findNavController().navigate(action)
    }

    private fun initObservers() {
        val owner = viewLifecycleOwner

        authViewModel.navigateToHome.observe(owner) { isLogged ->
            if (!isLogged) {
                navigateToLogin()
            }
        }
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun navigateToLogin() {
    }
}
