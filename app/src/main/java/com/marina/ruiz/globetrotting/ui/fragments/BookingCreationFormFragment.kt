package com.marina.ruiz.globetrotting.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.SelectorItem
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
                        binding.selectTraveler,
                        R.id.item_name,
                        travelers
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.destinations.collect { destinations ->
                    populateSelect(
                        binding.selectDestination,
                        R.id.item_name,
                        destinations
                    )
                }
            }
        }

        binding.calendar.setEndIconOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun <T : SelectorItem> populateSelect(
        textField: TextInputLayout?,
        textView: Int,
        items: List<T>
    ) {
        if (textField != null) {
            val adapter = object : ArrayAdapter<T>(
                requireContext(),
                R.layout.selector_item,
                textView,
                items
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)

                    val imageView = view.findViewById<ImageView>(R.id.item_img)
                    val textViewName = view.findViewById<TextView>(R.id.item_name)

                    val item = items[position]
                    if (item.image != null) {
                        imageView.load(item.image)
                    } else if (item.imageRef != null) {
                        imageView.setImageResource(item.imageRef as Int)
                    }
                    textViewName.text = item.name
                    return view
                }
            }
            val autoCompleteTv = (textField.editText as? AutoCompleteTextView)
            autoCompleteTv?.setAdapter(adapter)
            // Add a click listener for each item of the selector
            addSelectorClickListener(autoCompleteTv, items)
        }
    }

    /**
     * Add a click listener for each element of the selector list. When an item is selected
     * it displays the name (String) of the item.
     */
    private fun <T : SelectorItem> addSelectorClickListener(
        autocompleteTv: AutoCompleteTextView?,
        items: List<T>
    ) {
        if (autocompleteTv != null) {
            autocompleteTv.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    autocompleteTv.setText(items[position].name, false)
                }
        }
    }

    /**
     * Display a range datePicker on screen.
     */
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
        val departureDate = dateFormat.format(Date(startMillis))
        val arrivalDate = dateFormat.format(Date(endMillis))
        return "$departureDate - $arrivalDate"
    }
}