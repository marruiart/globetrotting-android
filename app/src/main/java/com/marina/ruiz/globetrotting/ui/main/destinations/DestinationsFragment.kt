package com.marina.ruiz.globetrotting.ui.main.destinations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.marina.ruiz.globetrotting.core.extension.hideKeyboard
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationsBinding
import com.marina.ruiz.globetrotting.ui.main.destinations.adapter.DestinationsListAdapter
import com.marina.ruiz.globetrotting.ui.main.destinations.model.BookingForm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DestinationsFragment : Fragment(), BookingCreationFormDialogListener,
    DestinationDetailDialogListener {
    private lateinit var binding: FragmentDestinationsBinding
    private lateinit var adapter: DestinationsListAdapter
    private lateinit var bookingDialog: BookingCreationFormDialogFragment
    private lateinit var detailDialog: DestinationDetailDialog
    private val destinationsVM: DestinationsViewModel by activityViewModels()

    companion object {
        private const val TAG = "GLOB_DEBUG DESTINATIONS_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDestinationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        bindView()
        Log.d(TAG, "created - destinationsVM.onlyFavorites ${destinationsVM.onlyFavorites}")
    }

    override fun onResume() {
        binding.cbFavoriteToggleFilter.setOnCheckedChangeListener(null)
        Log.d(TAG, "resume - destinationsVM.onlyFavorites ${destinationsVM.onlyFavorites}")
        binding.cbFavoriteToggleFilter.isChecked = destinationsVM.onlyFavorites

        binding.cbFavoriteToggleFilter.setOnCheckedChangeListener { _, isChecked ->
            destinationsVM.onlyFavorites = isChecked
            Log.d(TAG, "listener - isChecked $isChecked")
            Log.d(TAG, "listener - destinationsVM.onlyFavorites ${destinationsVM.onlyFavorites}")
            destinationsVM.bindView(adapter)
        }
        super.onResume()
    }

    /**
     * Initializes the adapter for the RecyclerView.
     */
    private fun initAdapter() {
        adapter = DestinationsListAdapter(::onShowDetail, ::onBookNow, ::onFavoriteDestination)
        val rv = binding.rvDestinationsList
        rv.adapter = adapter
    }

    /**
     * Binds the view elements to their respective actions and data.
     */
    private fun bindView() {
        Log.d(
            TAG,
            "bindView - destinationsVM.onlyFavorites ${binding.cbFavoriteToggleFilter.isChecked}"
        )
        destinationsVM.bindView(adapter)

        binding.btnRemoveFilter.setOnClickListener {
            binding.etDestinationsFilter.setText("")
        }

        binding.etDestinationsFilter.doAfterTextChanged { textView ->
            performSearch(textView.toString())
            if (textView.isNullOrEmpty()) {
                binding.btnRemoveFilter.visibility = View.GONE
            } else {
                binding.btnRemoveFilter.visibility = View.VISIBLE
            }
        }
        binding.etDestinationsFilter.setOnEditorActionListener { textView, actionId, _ ->
            this.hideKeyboard()
            true
        }
    }

    /**
     * Performs a search based on the provided query.
     *
     * @param searchQuery The search query to filter the destinations.
     */
    private fun performSearch(searchQuery: String) {
        Log.i(TAG, "searchQuery: $searchQuery")
        destinationsVM.bindView(adapter, searchQuery)
    }

    /**
     * Shows the detail dialog for the selected destination.
     *
     * @param destination The selected destination.
     */
    private fun onShowDetail(destination: Destination) {
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
        detailDialog = DestinationDetailDialog(this, destination)
        detailDialog.show(
            requireActivity().supportFragmentManager, "DestinationDetailDialog"
        )
    }

    /**
     * Handles the booking creation event.
     *
     * @param booking The booking form data.
     */
    override fun onMakeBooking(booking: BookingForm) {
        destinationsVM.makeBooking(booking.toBookingPayload(destinationsVM.user))
        bookingDialog.dismiss()
    }

    /**
     * Handles the booking cancellation event.
     */
    override fun onCancelBooking() {
        bookingDialog.dismiss()
    }

    /**
     * Handles the event of closing the details dialog.
     */
    override fun onCloseDetails() {
        detailDialog.dismiss()
    }

    /**
     * Called when the Fragment is no longer resumed.
     */
    override fun onPause() {
        binding.cbFavoriteToggleFilter.setOnCheckedChangeListener(null)
        destinationsVM.onlyFavorites = false
        binding.cbFavoriteToggleFilter.isChecked = false
        Log.d(TAG, "pause - destinationsVM.onlyFavorites ${destinationsVM.onlyFavorites}")
        super.onPause()
    }

}
