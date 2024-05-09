package com.marina.ruiz.globetrotting.ui.main.destinations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.ItemDestinationBinding
import java.text.NumberFormat

class DestinationsListAdapter(
    private val onShowDetail: (destination: Destination) -> Unit,
    private val onBookNow: (destination: Destination) -> Unit
) : ListAdapter<Destination, DestinationsListAdapter.DestinationViewHolder>(DestinationDiffCallBack()) {

    inner class DestinationViewHolder(
        private val binding: ItemDestinationBinding, private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindDestination(destination: Destination) {
            if (destination.imageRef != null) {
                binding.ivItemDestinationBackground.setImageResource(destination.imageRef)
            }
            binding.tvItemDestinationName.text = destination.name
            binding.tvItemDestinationShortDescription.text = destination.shortDescription
            binding.tvItemDestinationPrice.text = context.getString(
                R.string.destination_item_price,
                NumberFormat.getCurrencyInstance().format(destination.price)
            )
            binding.mcvItemDestination.setOnClickListener {
                onShowDetail(destination)
            }
            binding.btnItemDestinationBookNow.setOnClickListener {
                onBookNow(destination)
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
        val binding =
            ItemDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DestinationViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = getItem(position)
        holder.bindDestination(destination)
    }
}