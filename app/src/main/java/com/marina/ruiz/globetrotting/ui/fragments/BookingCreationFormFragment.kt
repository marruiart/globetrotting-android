package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.marina.ruiz.globetrotting.databinding.FragmentBookingCreationFormBinding
import com.marina.ruiz.globetrotting.databinding.FragmentCharacterDetailBinding

class BookingCreationFormFragment : Fragment() {
    private lateinit var binding: FragmentBookingCreationFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingCreationFormBinding
            .inflate(inflater, container, false)
        Log.d("FORM", "wiii")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}