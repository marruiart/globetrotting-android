package com.marina.ruiz.globetrotting.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.ItemPopularDestinationsHomeBinding

class PopularDestinationsAdapter(
    private val onShowDetail: (destination: Destination) -> Unit
) : ListAdapter<Destination, PopularDestinationsAdapter.DestinationViewHolder>(
    DestinationDiffCallBack()
) {

    inner class DestinationViewHolder(
        private val binding: ItemPopularDestinationsHomeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindDestination(destination: Destination) {
            if (destination.imageRef != null) {
                binding.ivBackgroundHome.setImageResource(destination.imageRef)
            }
            binding.tvPopularDestinationName.text = destination.name
            binding.mcvPopularDestinationItem.setOnClickListener {
                onShowDetail(destination)
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
        val binding = ItemPopularDestinationsHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DestinationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = getItem(position)
        holder.bindDestination(destination)
    }

}