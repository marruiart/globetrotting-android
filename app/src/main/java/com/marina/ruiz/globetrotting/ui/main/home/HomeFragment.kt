package com.marina.ruiz.globetrotting.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentHomeBinding
import com.marina.ruiz.globetrotting.ui.main.MainViewModel
import com.marina.ruiz.globetrotting.ui.main.home.adapter.PopularDestinationsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PopularDestinationsAdapter
    private val mainVM: MainViewModel by activityViewModels()
    private val homeVM: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        mainVM.setActionBarSizeMargin(requireActivity(), 0)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        homeVM.bindView(adapter)
    }

    private fun initUI() {
        initListeners()
        initAdapter()
    }

    private fun initListeners() {
        with(binding) {
            btnFavoritesHome.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToSeeAllDestinationsFragment()
                navigate(action)
            }
            btnMyAccountHome.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToProfileActivity()
                navigate(action)
            }
            btnMyBookingsHome.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToBookingsFragmentFromHome()
                navigate(action)
            }
            btnSeeAllDestinationsHome.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToSeeAllDestinationsFragment()
                navigate(action)
            }
        }
    }

    private fun initAdapter() {
        adapter = PopularDestinationsAdapter(::onShowDetail)
        val rv = binding.rvPopularDestinations
        rv.adapter = adapter
    }

    private fun onShowDetail(destination: Destination) {
        val action = HomeFragmentDirections.actionHomeFragmentToDestinationDetailFragment(
            destination
        )
        navigate(action)
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

}