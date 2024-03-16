package com.marina.ruiz.globetrotting.ui.bookings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.FragmentBookingsBinding
import com.marina.ruiz.globetrotting.ui.bookings.adapter.BookingsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class BookingsFragment : Fragment() {
    private lateinit var binding: FragmentBookingsBinding
    private val viewModel: BookingsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingsBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adapter = BookingsListAdapter(::onShareItem, ::onDeleteBooking, ::onUpdateBooking)
        bindView(adapter, view)
    }

    private fun bindView(adapter: BookingsListAdapter, view: View) {
        val rv = binding.bookingsList
        rv.adapter = adapter
        if (viewModel.bookings.value.isEmpty()) {
            displayNoBookingsMessage(view)
        } else {
            displayBookingsList()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookings.collect {
                    if (it.isNotEmpty()) {
                        adapter.submitList(it)
                        displayBookingsList()
                    } else {
                        displayNoBookingsMessage(view)
                    }
                }
            }
        }
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

    private fun navigateToBookingForm(view: View, destination: Destination, booking: Booking? = null) {
        val action =
            BookingsFragmentDirections.actionBookingsFragmentToBookingCreationFormFragment(destination, booking)
        view.findNavController().navigate(action)
    }

    private fun displayBookingsList() {
        binding.bookingsRecycler.visibility = View.VISIBLE
        binding.noBookingImg.visibility = View.GONE
        binding.noBookingText.visibility = View.GONE
        binding.noBookingBtn.visibility = View.GONE
    }

    private fun onShareItem(booking: Booking, view: View) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val departureDate = dateFormat.format(Date(booking.departureDate))
        val arrivalDate = dateFormat.format(Date(booking.arrivalDate))
        val shareText =
            getString(R.string.share_text, booking.destination.name, departureDate, arrivalDate)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND // this intent is going to send sth
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, null) // shows all apps available to share
        startActivity(shareIntent)
    }

    private fun onUpdateBooking(view: View, booking: Booking) {
        navigateToBookingForm(view, booking.destination, booking)
    }

    private fun onDeleteBooking(booking: Booking) {
        lifecycleScope.launch {
            viewModel.deleteBooking(booking).collect {
                Toast.makeText(
                    context,
                    getString(R.string.bookings_booking_deleted_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}