package com.marina.ruiz.globetrotting.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.marina.ruiz.globetrotting.data.repository.model.Character
import com.marina.ruiz.globetrotting.databinding.CharacterItemBinding

class CharacterAdapter(
    private val onShowDetail: (p: Character, view: View) -> Unit
) : ListAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallBack()) {

    inner class CharacterViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindCharacter(c: Character) {
            binding.charName.text = c.name
            binding.charImg.load(c.image)
            binding.charSpecies.text = c.species
            binding.charGender.text = c.gender
            binding.card.setOnClickListener {
                onShowDetail(c, binding.root)
            }
        }
    }

    private class CharacterDiffCallBack : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
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