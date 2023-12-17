package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
import com.marina.ruiz.globetrotting.databinding.FragmentTravelerDetailBinding
import com.marina.ruiz.globetrotting.ui.viewmodels.TravelerDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TravelerDetailFragment : Fragment() {
    private lateinit var binding: FragmentTravelerDetailBinding
    private val args: TravelerDetailFragmentArgs by navArgs()
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
        fetchDescription()
        bindView(args.traveler)
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

    private fun fetchDescription() {
        val resource = getResource(args.traveler.name)
        if (resource != -1) {
            val description = resources.getString(resource)
            viewModel.updateDescription(args.traveler, description)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.traveler.collect { traveler ->
                    bindView(traveler)
                }
            }
        }
    }

    private fun getResource(name: String): Int {
        return when (name) {
            "Rick Sanchez" -> R.string.RickSanchez
            "Morty Smith" -> R.string.MortySmith
            "Summer Smith" -> R.string.SummerSmith
            "Beth Smith" -> R.string.BethSmith
            "Jerry Smith" -> R.string.JerrySmith
            "Abadango Cluster Princess" -> R.string.AbadangoClusterPrincess
            "Abradolf Lincler" -> R.string.AbradolfLincler
            "Adjudicator Rick" -> R.string.AdjudicatorRick
            "Agency Director" -> R.string.AgencyDirector
            "Alan Rails" -> R.string.AlanRails
            "Albert Einstein" -> R.string.AlbertEinstein
            "Alexander" -> R.string.Alexander
            "Alien Googah" -> R.string.AlienGoogah
            "Alien Morty" -> R.string.AlienMorty
            "Alien Rick" -> R.string.AlienRick
            "Amish Cyborg" -> R.string.AmishCyborg
            "Annie" -> R.string.Annie
            "Antenna Morty" -> R.string.AntennaMorty
            "Antenna Rick" -> R.string.AntennaRick
            "Ants in my Eyes Johnson" -> R.string.AntsinmyEyesJohnson
            else -> -1
        }
    }
}