package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.databinding.FragmentBookingCreationFormBinding
import com.marina.ruiz.globetrotting.databinding.FragmentDestinationsBinding

class DestinationsFragment : Fragment() {
    private lateinit var binding: FragmentDestinationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDestinationsBinding
            .inflate(inflater, container, false)
        Log.d("FORM", "wiii")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}