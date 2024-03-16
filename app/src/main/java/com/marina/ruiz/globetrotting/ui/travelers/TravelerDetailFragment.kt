package com.marina.ruiz.globetrotting.ui.travelers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import coil.load
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import com.marina.ruiz.globetrotting.databinding.FragmentTravelerDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelerDetailFragment : Fragment() {
    private lateinit var binding: FragmentTravelerDetailBinding
    private val viewModel: TravelerDetailViewModel by viewModels()

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
        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.topAppBar)
    }

    private fun bindView(traveler: Traveler) {
        binding.topAppBar.setNavigationOnClickListener {
            navigateBack()
        }
        binding.travelerName.text = traveler.name
        binding.travelerImg.load(traveler.image)
        if (traveler.type != "") {
            binding.travelerType.text = traveler.type
        } else {
            binding.travelerType.visibility = View.GONE
        }
        binding.travelerDescription.text = traveler.description
        binding.travelerSpecies.text = traveler.species
        binding.travelerGender.text = traveler.gender
    }

    private fun navigateBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}