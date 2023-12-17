package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.marina.ruiz.globetrotting.databinding.FragmentTravelerDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelerDetailFragment : Fragment() {
    private lateinit var binding: FragmentTravelerDetailBinding
    private val args: TravelerDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTravelerDetailBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
    }

    private fun bindView() {
        binding.travelerName.text = args.traveler.name
        binding.travelerImg.load(args.traveler.image)
        binding.travelerSpecies.text = args.traveler.species
        binding.travelerGender.text = args.traveler.gender
    }
}