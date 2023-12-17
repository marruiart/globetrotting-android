package com.marina.ruiz.globetrotting.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.DestinationItemBinding

class DestinationsListAdapter(
    private val onShowDetail: (destination: Destination, view: View) -> Unit,
    private val onBookNow: (destination: Destination, view: View) -> Unit
) :
    ListAdapter<Destination, DestinationsListAdapter.DestinationViewHolder>(DestinationDiffCallBack()) {

    inner class DestinationViewHolder(private val binding: DestinationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindDestination(destination: Destination) {
            if (destination.imageRef != null) {
                binding.destinationImg.setImageResource(destination.imageRef)
            }
            binding.destinationName.text = destination.name
            binding.shortDescription.text = destination.shortDescription
            binding.destinationItem.setOnClickListener {
                onShowDetail(destination, binding.root)
            }
            binding.detailsBtn.setOnClickListener {
                onShowDetail(destination, binding.root)
            }
            binding.bookBtn.setOnClickListener {
                onBookNow(destination, binding.root)
            }
        }
    }

    private class DestinationDiffCallBack : DiffUtil.ItemCallback<Destination>() {
        override fun areItemsTheSame(oldItem: Destination, newItem: Destination): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Destination, newItem: Destination): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val binding = DestinationItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DestinationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = getItem(position)
        holder.bindDestination(destination)
    }
}