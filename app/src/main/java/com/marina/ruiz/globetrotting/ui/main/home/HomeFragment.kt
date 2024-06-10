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
import com.google.android.material.R as materialRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.changeStatusBarContrastStyle
import com.marina.ruiz.globetrotting.core.getColorFromThemeAttribute
import com.marina.ruiz.globetrotting.core.setOverflowButtonColor
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentHomeBinding
import com.marina.ruiz.globetrotting.ui.main.MainViewModel
import com.marina.ruiz.globetrotting.ui.main.destinations.BookingCreationFormDialogFragment
import com.marina.ruiz.globetrotting.ui.main.destinations.BookingCreationFormDialogListener
import com.marina.ruiz.globetrotting.ui.main.destinations.DestinationDetailDialog
import com.marina.ruiz.globetrotting.ui.main.destinations.DestinationDetailDialogListener
import com.marina.ruiz.globetrotting.ui.main.destinations.DestinationsViewModel
import com.marina.ruiz.globetrotting.ui.main.destinations.adapter.DestinationsListAdapter
import com.marina.ruiz.globetrotting.ui.main.destinations.model.BookingForm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), BookingCreationFormDialogListener,
    DestinationDetailDialogListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: DestinationsListAdapter
    private lateinit var detailDialog: DestinationDetailDialog
    private lateinit var bookingDialog: BookingCreationFormDialogFragment
    private var bottomNav: BottomNavigationView? = null
    private val mainVM: MainViewModel by activityViewModels()
    private val homeVM: HomeViewModel by viewModels()
    private val destinationsVM: DestinationsViewModel by activityViewModels()


    companion object {
        private const val TAG = "GLOB_DEBUG HOME_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bottomNav = activity?.findViewById(R.id.bnv_navigation)
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
            requireContext(), materialRes.attr.colorOnSurfaceVariant
        )
        setOverflowButtonColor(requireActivity(), newColor)
        changeStatusBarContrastStyle(requireActivity().window, lightIcons)
    }

    private fun initUI() {
        initListeners()
        initAdapter()
    }

    private fun initListeners() {
        favoritesListener()
        myAccountListener()
        myBookingsListener()
        seeAllDestinationsListener()
    }

    private fun favoritesListener() {
        binding.btnFavoritesHome.setOnClickListener {
            navigateToDestinations()
        }
    }

    private fun navigateToDestinations() {
        bottomNav?.selectedItemId = R.id.nav_destinations
    }

    private fun myAccountListener() {
        binding.btnMyAccountHome.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeFragmentToProfileActivity())
        }
    }

    private fun myBookingsListener() {
        binding.btnMyBookingsHome.setOnClickListener {
            bottomNav?.selectedItemId = R.id.nav_bookings
        }
    }

    private fun seeAllDestinationsListener() {
        binding.btnSeeAllDestinationsHome.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeFragmentToSeeAllDestinationsFragment())
        }
    }

    private fun initAdapter() {
        adapter =
            DestinationsListAdapter(::onShowDetail, ::onBookNow, ::onFavoriteDestination, true)
        val rv = binding.rvPopularDestinations
        rv.adapter = adapter
    }

    private fun onShowDetail(destination: Destination) {
        setStatusBarAndOverflowButtonColor()
        showDestinationDetailDialog(destination)
    }

    private fun onBookNow(destination: Destination) {
        showBookNowDialog(destination)
    }

    private fun onFavoriteDestination(destinationId: String, isFavorite: Boolean) {
        destinationsVM.handleFavorite(destinationId, isFavorite)
    }

    private fun showBookNowDialog(destination: Destination) {
        setStatusBarAndOverflowButtonColor()
        bookingDialog = BookingCreationFormDialogFragment(this, destination)
        bookingDialog.show(
            requireActivity().supportFragmentManager, "BookingCreationFormDialog"
        )
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

    override fun onMakeBooking(booking: BookingForm) {
        destinationsVM.makeBooking(booking.toBookingPayload(destinationsVM.user))
        bookingDialog.dismiss()
    }

    override fun onCancelBooking() {
        bookingDialog.dismiss()
    }

    override fun onCloseDetails() {
        detailDialog.dismiss()
        setStatusBarAndOverflowButtonColor(Color.WHITE, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        setStatusBarAndOverflowButtonColor()
        mainVM.setActionBarSizeMargin(requireActivity(), 1)
    }

}