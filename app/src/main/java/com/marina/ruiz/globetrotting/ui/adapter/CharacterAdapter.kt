package com.marina.ruiz.globetrotting.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import com.marina.ruiz.globetrotting.databinding.CharacterItemBinding

class CharacterAdapter(
    private val onShowDetail: (p: Traveler, view: View) -> Unit
) : ListAdapter<Traveler, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallBack()) {

    inner class CharacterViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCharacter(c: Traveler) {
            binding.charName.text = c.name
            binding.charImg.load(c.image)
            binding.charSpecies.text = c.species
            binding.charGender.text = c.gender
            binding.card.setOnClickListener {
                onShowDetail(c, binding.root)
            }
        }
    }

    private class CharacterDiffCallBack : DiffUtil.ItemCallback<Traveler>() {
        override fun areItemsTheSame(oldItem: Traveler, newItem: Traveler): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Traveler, newItem: Traveler): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = CharacterItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        holder.bindCharacter(character)
    };


}