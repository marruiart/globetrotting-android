package com.marina.ruiz.globetrotting.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import com.marina.ruiz.globetrotting.databinding.TravelerItemBinding

class TravelerListAdapter(
    private val onShowDetail: (p: Traveler, view: View) -> Unit
) : ListAdapter<Traveler, TravelerListAdapter.TravelerViewHolder>(TravelerDiffCallBack()) {

    inner class TravelerViewHolder(private val binding: TravelerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTraveler(c: Traveler) {
            binding.travelerName.text = c.name
            binding.travelerImg.load(c.image)
            binding.card.setOnClickListener {
                onShowDetail(c, binding.root)
            }
        }
    }

    private class TravelerDiffCallBack : DiffUtil.ItemCallback<Traveler>() {
        override fun areItemsTheSame(oldItem: Traveler, newItem: Traveler): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Traveler, newItem: Traveler): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelerViewHolder {
        val binding = TravelerItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TravelerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelerViewHolder, position: Int) {
        val traveler = getItem(position)
        holder.bindTraveler(traveler)
    };


}