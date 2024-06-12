package com.marina.ruiz.globetrotting.ui.main.bookings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.databinding.FragmentBookingsBinding
import com.marina.ruiz.globetrotting.ui.main.bookings.adapter.BookingsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class BookingsFragment : Fragment() {
    private lateinit var binding: FragmentBookingsBinding
    private lateinit var adapter: BookingsListAdapter
    private var bottomNav: BottomNavigationView? = null
    private val bookingsVM: BookingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingsBinding.inflate(inflater, container, false)
        bottomNav = activity?.findViewById(R.id.bnv_navigation)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers(view)
        bookingsVM.bindView(adapter)
    }


    /**
     * Initializes the observers for the ViewModel.
     *
     * @param view The view used to display the fragment's UI
     */
    private fun initObservers(view: View) {
        bookingsVM.showNoBookingDialog.observe(viewLifecycleOwner) { show ->
            if (show) {
                displayNoBookingsMessage(view)
            } else {
                displayBookingsList()
            }
        }
    }

    /**
     * Initializes the adapter for the RecyclerView.
     */
    private fun initAdapter() {
        adapter = BookingsListAdapter(::onShareItem)
        val rv = binding.rvBookingsList
        rv.adapter = adapter
    }

    /**
     * Displays a message indicating that there are no bookings.
     *
     * @param view The view used to display the fragment's UI
     */
    private fun displayNoBookingsMessage(view: View) {
        binding.bookingsRecycler.visibility = View.GONE
        binding.noBookingImg.visibility = View.VISIBLE
        binding.noBookingText.visibility = View.VISIBLE
        binding.btnBookingsNoBooking.visibility = View.VISIBLE
        binding.btnBookingsNoBooking.setOnClickListener {
            navigateToDestinations()
        }
    }

    private fun navigateToDestinations() {
        bottomNav?.selectedItemId = R.id.nav_destinations
    }


    private fun displayBookingsList() {
        binding.bookingsRecycler.visibility = View.VISIBLE
        binding.noBookingImg.visibility = View.GONE
        binding.noBookingText.visibility = View.GONE
        binding.btnBookingsNoBooking.visibility = View.GONE
    }

    /**
     * Handles the sharing of a booking item.
     *
     * @param booking The booking item to be shared
     * @param view The view used to display the booking item
     */
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

}