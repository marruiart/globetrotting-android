package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.marina.ruiz.globetrotting.databinding.FragmentBookingCreationFormBinding
import com.marina.ruiz.globetrotting.ui.viewmodels.BookingFormViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class BookingCreationFormFragment : Fragment() {
    private lateinit var binding: FragmentBookingCreationFormBinding
    private val viewModel: BookingFormViewModel by viewModels()
    private var departureDate: Long = 0
    private var arrivalDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingCreationFormBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.travelers.collect { travelers ->
                    populateSelect(
                        binding.selectTraveler.editText,
                        travelers.map { traveler -> traveler.name }.toTypedArray()
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.destinations.collect { destinations ->
                    populateSelect(
                        binding.selectDestination.editText,
                        destinations.map { destination -> destination.name }.toTypedArray()
                    )
                }
            }
        }

        binding.calendar.setEndIconOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .build()

        dateRangePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            departureDate = selection.first ?: 0
            arrivalDate = selection.second ?: 0
            val formattedDateRange = formatDateRange(departureDate, arrivalDate)
            binding.calendarText.setText(formattedDateRange)
        }
    }

    private fun formatDateRange(startMillis: Long, endMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val startDate = dateFormat.format(Date(startMillis))
        val endDate = dateFormat.format(Date(endMillis))
        return "$startDate - $endDate"
    }

    private fun populateSelect(select: EditText?, items: Array<String>) {
        if (select != null) {
            (select as? MaterialAutoCompleteTextView)?.setSimpleItems(items)
        }
    }
}