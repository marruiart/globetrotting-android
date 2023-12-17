package com.marina.ruiz.globetrotting.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.databinding.FragmentBookingsBinding
import com.marina.ruiz.globetrotting.ui.adapter.BookingsListAdapter
import com.marina.ruiz.globetrotting.ui.viewmodels.BookingsListViewModel
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

        var adapter = BookingsListAdapter(::onShareItem)
        bindView(adapter, view)
    }

    private fun bindView(adapter: BookingsListAdapter, view: View) {
        val rv = binding.bookingsList
        rv.adapter = adapter
        if (viewModel.bookings.value.isEmpty()) {
            binding.bookingsRecycler.visibility = View.GONE
            binding.noBookingImg.visibility = View.VISIBLE
            binding.noBookingText.visibility = View.VISIBLE
            binding.noBookingBtn.visibility = View.VISIBLE
            binding.noBookingBtn.setOnClickListener {
                val action =
                    BookingsFragmentDirections.actionBookingsFragmentToBookingCreationFormFragment()
                view.findNavController().navigate(action)
            }
        } else {
            binding.bookingsRecycler.visibility = View.VISIBLE
            binding.noBookingImg.visibility = View.GONE
            binding.noBookingText.visibility = View.GONE
            binding.noBookingBtn.visibility = View.GONE
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookings.collect {
                    adapter.submitList(it)
                }
            }
        }
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
}