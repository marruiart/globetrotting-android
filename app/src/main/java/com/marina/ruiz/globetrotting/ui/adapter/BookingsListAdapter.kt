package com.marina.ruiz.globetrotting.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.databinding.BookingItemBinding

class BookingsListAdapter() :
    ListAdapter<Booking, BookingsListAdapter.BookingViewHolder>(BookingDiffCallBack()) {

    inner class BookingViewHolder(private val binding: BookingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindBooking(booking: Booking) {

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
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = getItem(position)
        holder.bindBooking(booking)
    }
}