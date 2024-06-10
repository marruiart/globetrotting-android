package com.marina.ruiz.globetrotting.ui.main.destinations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
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
    }

    private fun initAdapter() {
        adapter = DestinationsListAdapter(::onShowDetail, ::onBookNow, ::onFavoriteDestination)
        val rv = binding.rvDestinationsList
        rv.adapter = adapter
    }

    private fun bindView() {
        binding.cbFavoriteToggleFilter.isChecked = destinationsVM.onlyFavorites
        destinationsVM.bindView(adapter)
        binding.cbFavoriteToggleFilter.setOnCheckedChangeListener { _, isChecked ->
            destinationsVM.onlyFavorites = isChecked
            destinationsVM.bindView(adapter)
        }
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

    private fun performSearch(searchQuery: String) {
        Log.i(TAG, "searchQuery: $searchQuery")
        destinationsVM.bindView(adapter, searchQuery)
    }

    private fun onShowDetail(destination: Destination) {
        showDestinationDetailDialog(destination)
    }

    private fun onBookNow(destination: Destination) {
        showBookNowDialog(destination)
    }

    private fun onFavoriteDestination(destinationId: String, isFavorite: Boolean) {
        destinationsVM.handleFavorite(destinationId, isFavorite)
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun showBookNowDialog(destination: Destination) {
        bookingDialog = BookingCreationFormDialogFragment(this, destination)
        bookingDialog.show(
            requireActivity().supportFragmentManager, "BookingCreationFormDialog"
        )
    }

    private fun showDestinationDetailDialog(destination: Destination) {
        detailDialog = DestinationDetailDialog(this, destination)
        detailDialog.show(
            requireActivity().supportFragmentManager, "DestinationDetailDialog"
        )
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
    }

    override fun onDestroy() {
        destinationsVM.onlyFavorites = false
        super.onDestroy()
    }
}
