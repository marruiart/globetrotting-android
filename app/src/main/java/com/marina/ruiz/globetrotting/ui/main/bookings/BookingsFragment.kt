package com.marina.ruiz.globetrotting.ui.main.bookings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.R.attr.colorOnSurfaceVariant
import com.google.android.material.appbar.MaterialToolbar
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.getColorFromThemeAttribute
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentBookingsBinding
import com.marina.ruiz.globetrotting.ui.main.MainViewModel
import com.marina.ruiz.globetrotting.ui.main.bookings.adapter.BookingsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class BookingsFragment : Fragment() {
    private lateinit var binding: FragmentBookingsBinding
    private lateinit var adapter: BookingsListAdapter
    private val bookingsVM: BookingsViewModel by activityViewModels()
    private val mainVM: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingsBinding.inflate(inflater, container, false)
        mainVM.setActionBarSizeMargin(requireActivity(), 1)
        setOverflowButtonColor()
        return binding.root
    }

    private fun setOverflowButtonColor() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.mt_main_toolbar)
        val color = getColorFromThemeAttribute(requireContext(), colorOnSurfaceVariant)
        val overflowIcon = toolbar.overflowIcon
        overflowIcon?.setTint(color)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers(view)
        bookingsVM.bindView(adapter)
    }

    private fun initObservers(view: View) {
        bookingsVM.showNoBookingDialog.observe(viewLifecycleOwner) { show ->
            if (show) {
                displayNoBookingsMessage(view)
            } else {
                displayBookingsList()
            }
        }
    }

    private fun initAdapter() {
        adapter = BookingsListAdapter(::onShareItem, ::onDeleteBooking)
        val rv = binding.rvBookingsList
        rv.adapter = adapter
    }

    private fun displayNoBookingsMessage(view: View) {
        binding.bookingsRecycler.visibility = View.GONE
        binding.noBookingImg.visibility = View.VISIBLE
        binding.noBookingText.visibility = View.VISIBLE
        binding.noBookingBtn.visibility = View.VISIBLE
        binding.noBookingBtn.setOnClickListener {
            navigateToBookingForm(view, Destination())
        }
    }

    private fun navigateToBookingForm(
        view: View, destination: Destination, booking: Booking? = null
    ) {/*        val action =
                    BookingsFragmentDirections.actionBookingsFragmentToBookingCreationFormFragment(
                        destination,
                        booking
                    )
                view.findNavController().navigate(action)*/
    }

    private fun displayBookingsList() {
        binding.bookingsRecycler.visibility = View.VISIBLE
        binding.noBookingImg.visibility = View.GONE
        binding.noBookingText.visibility = View.GONE
        binding.noBookingBtn.visibility = View.GONE
    }

    private fun onShareItem(booking: Booking, view: View) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val departureDate = dateFormat.format(Date(booking.start))
        val arrivalDate = dateFormat.format(Date(booking.end))
        val shareText =
            getString(R.string.share_text, booking.destinationName, departureDate, arrivalDate)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND // this intent is going to send sth
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, null) // shows all apps available to share
        startActivity(shareIntent)
    }

    private fun onDeleteBooking(booking: Booking) {
        lifecycleScope.launch {
/*            bookingsVM.deleteBooking(booking).collect {
                Toast.makeText(
                    context,
                    getString(R.string.bookings_booking_deleted_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }*/
        }
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