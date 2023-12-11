package com.marina.ruiz.globetrotting.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.DestinationItemBinding

class DestinationsListAdapter() :
    ListAdapter<Destination, DestinationsListAdapter.DestinationViewHolder>(DestinationDiffCallBack()) {

    inner class DestinationViewHolder(private val binding: DestinationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindDestination(destination: Destination) {
            binding.destinationImg.setImageResource(chooseImage(destination.type))
            binding.destinationName.text = destination.name
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

    private fun chooseImage(type: String?): Int {
        return when (type) {
            "Planet" -> R.drawable.type_planet
            "Cluster" -> R.drawable.type_cluster
            "Space station" -> R.drawable.type_space_station
            "Microverse" -> R.drawable.type_microverse
            "TV" -> R.drawable.type_tv
            "Resort" -> R.drawable.type_resort
            "Fantasy town" -> R.drawable.type_fantasy
            "Dream" -> R.drawable.type_dream
            "Dimension" -> R.drawable.type_dimension
            "Menagerie" -> R.drawable.type_menagerie
            "Game" -> R.drawable.type_game
            //"Customs" -> R.drawable.type_customs
            "Daycare" -> R.drawable.type_daycare
            //"Dwarf planet (Celestial Dwarf)" -> R.drawable.type_dwarf
            "Miniverse" -> R.drawable.type_miniverse
            //"Teenyverse" -> R.drawable.type_teenyverse
            "Box" -> R.drawable.type_box
            "Spacecraft" -> R.drawable.type_spacecraft
            //"Artificially generated world" -> R.drawable.type_artifical
            //"Machine" -> R.drawable.type_machine
            //"Arcade" -> R.drawable.type_arcade
            //"Spa" -> R.drawable.type_spa
            else -> R.drawable.default_avatar
        }
    }


}