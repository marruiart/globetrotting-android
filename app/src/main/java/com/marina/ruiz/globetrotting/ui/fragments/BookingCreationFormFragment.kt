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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Booking
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.data.repository.model.SelectorItem
import com.marina.ruiz.globetrotting.data.repository.model.Traveler
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
    private lateinit var activity: FragmentActivity
    private val viewModel: BookingFormViewModel by viewModels()
    private val args: BookingCreationFormFragmentArgs by navArgs()
    private lateinit var booking: Booking
    private var destination: Destination? = null
    private var traveler: Traveler? = null
    private var departureDate: Long = 0
    private var arrivalDate: Long = 0
    private var numTravelers: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingCreationFormBinding.inflate(inflater, container, false)
        activity = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.topAppBar)
        init()
    }

    private fun init() {
        fetchItemsToPopulateSelects()
        displaySelectedItemOnSelector(binding.acSelectDestination, args.destination, true)
        setListeners()
    }

    private fun fetchItemsToPopulateSelects() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.travelers.collect { travelers ->
                    populateSelect(
                        binding.selectTraveler, R.id.booking_traveler_name, travelers
                    )
                }
            }
        }
    }

    private fun setListeners() {
        binding.calendar.setEndIconOnClickListener {
            showDatePickerDialog()
        }
        binding.numTravelersText.doAfterTextChanged { num ->
            val intNum: Int? = num.toString().toIntOrNull() ?: 0
            if (intNum != null) {
                numTravelers = intNum
            }
        }
        binding.acceptFormBtn.setOnClickListener {
            if (traveler != null && destination != null) {
                booking = Booking(
                    traveler as Traveler, destination as Destination, arrivalDate, departureDate, numTravelers
                )
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.createBooking(booking)
            }
            navigateBackToDestinations()
        }
    }

    private inline fun <reified T : SelectorItem> populateSelect(
        textField: TextInputLayout?, textView: Int, items: List<T>
    ) {
        if (textField != null) {
            val adapter = object : ArrayAdapter<T>(
                requireContext(), R.layout.selector_item, textView, items
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)

                    val imageView = view.findViewById<ImageView>(R.id.booking_traveler_img)
                    val textViewName = view.findViewById<TextView>(R.id.booking_traveler_name)

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
    private inline fun <reified T : SelectorItem> addSelectorClickListener(
        autocompleteTv: AutoCompleteTextView?, items: List<T>
    ) {
        if (autocompleteTv != null) {
            autocompleteTv.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val selectedItem = items[position]
                    displaySelectedItemOnSelector(autocompleteTv, selectedItem)
                }
        }
    }

    private inline fun <reified T : SelectorItem> displaySelectedItemOnSelector(
        autocompleteTv: AutoCompleteTextView, selectedItem: T, filter: Boolean = false
    ) {
        if (T::class == Destination::class) {
            destination = selectedItem as Destination
        } else if (T::class == Traveler::class) {
            traveler = selectedItem as Traveler
        }
        autocompleteTv.setText(selectedItem.name, filter)
    }

    /**
     * Display a range datePicker on screen.
     */
    private fun showDatePickerDialog() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker().build()

        dateRangePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            departureDate = selection.first ?: 0L
            arrivalDate = selection.second ?: 0L
            val formattedDateRange = formatDateRange(departureDate, arrivalDate)
            if (departureDate != 0L && arrivalDate != 0L) {
                departureDate = departureDate
                arrivalDate = arrivalDate
                binding.calendarText.setText(formattedDateRange)
            } else {
                Toast.makeText(
                    context, "Se deben seleccionar fechas de salida y llegada", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun formatDateRange(departureMillis: Long, arrivalMillis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val departureDate = dateFormat.format(Date(departureMillis))
        val arrivalDate = dateFormat.format(Date(arrivalMillis))
        return "$departureDate - $arrivalDate"
    }

    private fun navigateBackToDestinations() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}