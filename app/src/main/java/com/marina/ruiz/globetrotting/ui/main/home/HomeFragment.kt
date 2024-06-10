package com.marina.ruiz.globetrotting.ui.main.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.R
import com.marina.ruiz.globetrotting.core.changeStatusBarContrastStyle
import com.marina.ruiz.globetrotting.core.getColorFromThemeAttribute
import com.marina.ruiz.globetrotting.core.setOverflowButtonColor
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentHomeBinding
import com.marina.ruiz.globetrotting.ui.main.MainViewModel
import com.marina.ruiz.globetrotting.ui.main.destinations.DestinationDetailDialog
import com.marina.ruiz.globetrotting.ui.main.destinations.DestinationDetailDialogListener
import com.marina.ruiz.globetrotting.ui.main.home.adapter.PopularDestinationsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), DestinationDetailDialogListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PopularDestinationsAdapter
    private lateinit var detailDialog: DestinationDetailDialog
    private val mainVM: MainViewModel by activityViewModels()
    private val homeVM: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        mainVM.setActionBarSizeMargin(requireActivity(), 0)
        setStatusBarAndOverflowButtonColor(Color.WHITE, true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        homeVM.bindView(adapter)
    }

    private fun setStatusBarAndOverflowButtonColor(
        color: Int? = null, lightIcons: Boolean = false
    ) {
        val newColor = color ?: getColorFromThemeAttribute(
            requireContext(), R.attr.colorOnSurfaceVariant
        )
        setOverflowButtonColor(requireActivity(), newColor)
        changeStatusBarContrastStyle(requireActivity().window, lightIcons)
    }

    private fun initUI() {
        initListeners()
        initAdapter()
    }

    private fun initListeners() {
        with(binding) {
            btnFavoritesHome.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToFavoritesDestinationsActivity()
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
        setStatusBarAndOverflowButtonColor()
        showDestinationDetailDialog(destination)
    }

    private fun showDestinationDetailDialog(destination: Destination) {
        setStatusBarAndOverflowButtonColor()
        detailDialog = DestinationDetailDialog(this, destination)
        detailDialog.show(
            requireActivity().supportFragmentManager, "DestinationDetailDialog"
        )
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        setStatusBarAndOverflowButtonColor()
        mainVM.setActionBarSizeMargin(requireActivity(), 1)
    }

    override fun onCloseDetails() {
        detailDialog.dismiss()
        setStatusBarAndOverflowButtonColor(Color.WHITE, true)
    }

}