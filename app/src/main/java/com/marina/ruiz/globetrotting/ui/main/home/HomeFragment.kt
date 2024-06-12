package com.marina.ruiz.globetrotting.ui.main.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
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
import com.google.android.material.R as materialRes

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

    /**
     * Sets the status bar and overflow button color.
     *
     * @param color The color to set for the status bar and overflow button.
     * @param lightIcons Whether to use light icons on the status bar.
     */
    private fun setStatusBarAndOverflowButtonColor(
        color: Int? = null, lightIcons: Boolean = false
    ) {
        val newColor = color ?: getColorFromThemeAttribute(
            requireContext(), materialRes.attr.colorOnSurfaceVariant
        )
        setOverflowButtonColor(requireActivity(), newColor)
        changeStatusBarContrastStyle(requireActivity().window, lightIcons)
    }

    override fun onResume() {
        homeVM.bindView(adapter)
        super.onResume()
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

    /**
     * Sets up the listener for the favorites button.
     */
    private fun favoritesListener() {
        binding.btnFavoritesHome.setOnClickListener {
            navigateToDestinations()
        }
    }

    private fun navigateToDestinations() {
        destinationsVM.onlyFavorites = true
        Log.d(TAG, "destinationsVM.onlyFavorites ${destinationsVM.onlyFavorites}")
        bottomNav?.selectedItemId = R.id.nav_destinations
    }

    /**
     * Sets up the listener for the my account button.
     */
    private fun myAccountListener() {
        binding.btnMyAccountHome.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeFragmentToProfileActivity())
        }
    }

    /**
     * Sets up the listener for the my bookings button.
     */
    private fun myBookingsListener() {
        binding.btnMyBookingsHome.setOnClickListener {
            bottomNav?.selectedItemId = R.id.nav_bookings
        }
    }

    /**
     * Sets up the listener for the see all destinations button.
     */
    private fun seeAllDestinationsListener() {
        binding.btnSeeAllDestinationsHome.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeFragmentToSeeAllDestinationsFragment())
        }
    }

    /**
     * Initializes the adapter for the RecyclerView.
     */
    private fun initAdapter() {
        adapter = DestinationsListAdapter(
            ::onShowDetail,
            ::onBookNow,
            ::onFavoriteDestination,
            onlyPopular = true
        )
        val rv = binding.rvPopularDestinations
        rv.adapter = adapter
    }

    /**
     * Shows the detail dialog for the selected destination.
     *
     * @param destination The selected destination.
     */
    private fun onShowDetail(destination: Destination) {
        setStatusBarAndOverflowButtonColor()
        showDestinationDetailDialog(destination)
    }

    /**
     * Shows the booking dialog for the selected destination.
     *
     * @param destination The selected destination.
     */
    private fun onBookNow(destination: Destination) {
        showBookNowDialog(destination)
    }

    /**
     * Handles the favorite action for a destination.
     *
     * @param destinationId The ID of the destination.
     * @param isFavorite Indicates if the destination is marked as favorite.
     */
    private fun onFavoriteDestination(destinationId: String, isFavorite: Boolean) {
        destinationsVM.handleFavorite(destinationId, isFavorite)
    }

    /**
     * Shows the booking creation dialog for the selected destination.
     *
     * @param destination The selected destination.
     */
    private fun showBookNowDialog(destination: Destination) {
        setStatusBarAndOverflowButtonColor()
        bookingDialog = BookingCreationFormDialogFragment(this, destination)
        bookingDialog.show(
            requireActivity().supportFragmentManager, "BookingCreationFormDialog"
        )
    }

    /**
     * Shows the destination detail dialog for the selected destination.
     *
     * @param destination The selected destination.
     */
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