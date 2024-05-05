package com.marina.ruiz.globetrotting.ui.main.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationsBinding
import com.marina.ruiz.globetrotting.ui.main.MainViewModel
import com.marina.ruiz.globetrotting.ui.main.destinations.adapter.DestinationsListAdapter
import com.marina.ruiz.globetrotting.ui.main.destinations.model.BookingForm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DestinationsFragment : Fragment(), BookingCreationFormDialogFragmentListener {
    private lateinit var binding: FragmentDestinationsBinding
    private lateinit var adapter: DestinationsListAdapter
    private lateinit var dialog: BookingCreationFormDialogFragment
    private val destinationsVM: DestinationsViewModel by activityViewModels()
    private val mainVM: MainViewModel by activityViewModels()

    companion object {
        private const val TAG = "GLOB_DEBUG DESTINATIONS_FRAGMENT"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDestinationsBinding.inflate(inflater, container, false)
        mainVM.setActionBarSizeMargin(requireActivity(), 1)
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

    private fun onShowDetail(destination: Destination) {
        val action =
            DestinationsFragmentDirections.actionDestinationsFragmentToDestinationDetailFragment(
                destination
            )
        navigate(action)
    }

    private fun onBookNow(destination: Destination) {
        showBookNowDialog(destination)
    }

    private fun initObservers() {
        val owner = viewLifecycleOwner
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }

    private fun showBookNowDialog(destination: Destination) {
        dialog = BookingCreationFormDialogFragment(this, destination)
        dialog.show(requireActivity().supportFragmentManager, "BookingCreationFormDialogFragment")
    }

    override fun onMakeBooking(booking: BookingForm) {
        destinationsVM.makeBooking(booking.toBookingPayload(destinationsVM.user))
    }

    override fun onCancel() {
        dialog.dismiss()
    }
}
