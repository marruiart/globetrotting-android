package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationDetailBinding

class DestinationDetailFragment : Fragment() {
    private lateinit var binding: FragmentDestinationDetailBinding
    private val args: DestinationDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDestinationDetailBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.destinationName.text = args.destination.name
        binding.destinationImg.setImageResource(args.destination.imageRef)
    }
}