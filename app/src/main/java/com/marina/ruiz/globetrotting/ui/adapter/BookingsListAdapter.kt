package com.marina.ruiz.globetrotting.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.databinding.BookingItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookingsListAdapter(
    private val onShare: (booking: Booking, view: View) -> Unit,
    private val onDeleteBooking: (booking: Booking) -> Unit,
    private val onUpdateBooking: (view: View, booking: Booking) -> Unit
) :
    ListAdapter<Booking, BookingsListAdapter.BookingViewHolder>(BookingDiffCallBack()) {

    inner class BookingViewHolder(
        private val binding: BookingItemBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindBooking(booking: Booking) {
            binding.destinationName.text = booking.destination.name
            binding.bookingTravelerName.text = booking.traveler.name
            binding.bookingTravelerImg.load(booking.traveler.image)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val departureDate = dateFormat.format(Date(booking.departureDate))
            val arrivalDate = dateFormat.format(Date(booking.arrivalDate))
            binding.departureDate.text =
                context.getString(R.string.booking_item_departure, departureDate)
            binding.arrivalDate.text = context.getString(R.string.booking_item_arrival, arrivalDate)
            binding.shareBtn.setOnClickListener {
                onShare(booking, it)
            }
            binding.menuBtn.setOnClickListener { view ->
                showMenu(view, R.menu.more_actions_menu, booking)
            }
        }

        private fun showMenu(view: View, @MenuRes menuRes: Int, booking: Booking) {
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(menuRes, popup.menu)

            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.edit_booking -> {
                        onUpdateBooking(view, booking)
                        true
                    }

                    R.id.delete_booking -> {
                        onDeleteBooking(booking)
                        true
                    }

                    else -> false
                }
            }
            // Show the popup menu
            popup.show()
        }
    }

    private class BookingDiffCallBack : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = BookingItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = getItem(position)
        holder.bindBooking(booking)
    }
}