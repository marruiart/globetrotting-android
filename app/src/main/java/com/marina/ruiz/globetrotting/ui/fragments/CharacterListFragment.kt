package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import com.marina.ruiz.globetrotting.databinding.FragmentCharacterListBinding
import com.marina.ruiz.globetrotting.ui.adapter.CharacterAdapter
import com.marina.ruiz.globetrotting.ui.viewmodels.CharacterListViewModel

class CharacterListFragment : Fragment() {
    private lateinit var binding: FragmentCharacterListBinding
    private val viewModel: CharacterListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterListBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter = CharacterAdapter(::onShowDetail)
        binding.characterList.adapter = adapter

        // Subscribe to observable from viewModel
        val observer = Observer<List<Traveler>> {
            // update adapter
            Log.d("LISTA", it.toString())
            adapter.submitList(it)
        }

        viewModel.characters.observe(viewLifecycleOwner, observer)
    }

    private fun onShowDetail(character: Traveler, view: View) {
        val action =
            CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
                character
            )
        view.findNavController().navigate(action)
    }

}