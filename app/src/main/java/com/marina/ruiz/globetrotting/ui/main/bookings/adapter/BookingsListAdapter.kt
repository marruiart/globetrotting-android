package com.marina.ruiz.globetrotting.ui.main.bookings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.databinding.ItemBookingBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookingsListAdapter(
    private val onShare: (booking: Booking, view: View) -> Unit
) :
    ListAdapter<Booking, BookingsListAdapter.BookingViewHolder>(BookingDiffCallBack()) {

    inner class BookingViewHolder(
        private val binding: ItemBookingBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindBooking(booking: Booking) {
            binding.destinationName.text = booking.destinationName
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val departureDate = dateFormat.format(Date(booking.start))
            val arrivalDate = dateFormat.format(Date(booking.end))
            binding.departureDate.text =
                context.getString(R.string.booking_item_departure, departureDate)
            binding.arrivalDate.text = context.getString(R.string.booking_item_arrival, arrivalDate)
            binding.shareBtn.setOnClickListener {
                onShare(booking, it)
            }
        }
    }

    private class BookingDiffCallBack : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = getItem(position)
        holder.bindBooking(booking)
    }
}