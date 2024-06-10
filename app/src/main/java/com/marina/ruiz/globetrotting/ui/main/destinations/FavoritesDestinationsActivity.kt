package com.marina.ruiz.globetrotting.ui.main.destinations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.ActivityFavoritesDestinationsBinding
import com.marina.ruiz.globetrotting.databinding.ActivityProfileBinding
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationsBinding
import com.marina.ruiz.globetrotting.ui.main.destinations.adapter.DestinationsListAdapter
import com.marina.ruiz.globetrotting.ui.main.destinations.model.BookingForm
import com.marina.ruiz.globetrotting.ui.main.profile.ProfileActivity
import com.marina.ruiz.globetrotting.ui.main.profile.ProfileViewModel

class FavoritesDestinationsActivity : AppCompatActivity(), BookingCreationFormDialogListener,
    DestinationDetailDialogListener  {
    private lateinit var binding: ActivityFavoritesDestinationsBinding
    private lateinit var adapter: DestinationsListAdapter
    private lateinit var bookingDialog: BookingCreationFormDialogFragment
    private lateinit var detailDialog: DestinationDetailDialog
    private lateinit var systemBars: Insets
    private val destinationsVM: DestinationsViewModel by viewModels()

    companion object {
        private const val TAG = "GLOB_DEBUG FAVORITES_DESTINATIONS_ACTIVITY"

        fun create(context: Context): Intent = Intent(context, FavoritesDestinationsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoritesDestinationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setWindowInsets()
        initAdapter()
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.favorites_destinations)) { v, insets ->
            systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initAdapter() {
        adapter = DestinationsListAdapter(::onShowDetail, ::onBookNow, ::onFavoriteDestination)
        val rv = binding.rvFavoritesDestinationsList
        rv.adapter = adapter
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

    private fun showBookNowDialog(destination: Destination) {
        bookingDialog = BookingCreationFormDialogFragment(this, destination)
        bookingDialog.show(
            supportFragmentManager, "BookingCreationFormDialog"
        )
    }

    private fun showDestinationDetailDialog(destination: Destination) {
        detailDialog = DestinationDetailDialog(this, destination)
        detailDialog.show(
            supportFragmentManager, "DestinationDetailDialog"
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
}