package com.marina.ruiz.globetrotting.ui.main.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Timestamp
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.core.dialog.FullScreenDialogFragment
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.DialogBookingCreationFormBinding
import com.marina.ruiz.globetrotting.ui.main.destinations.model.BookingForm
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface BookingCreationFormDialogListener {
    fun onMakeBooking(booking: BookingForm)
    fun onCancelBooking()
}

class BookingCreationFormDialogFragment(
    private val callback: BookingCreationFormDialogListener, destination: Destination
) : FullScreenDialogFragment(R.layout.dialog_booking_creation_form) {
    private val padding = 16
    private val maxTravelers = 10
    private val minTravelers = 1
    private lateinit var binding: DialogBookingCreationFormBinding
    private lateinit var activity: FragmentActivity
    private var form = BookingForm(destination)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overrideOnBackPressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogBookingCreationFormBinding.inflate(layoutInflater)
        activity = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets()
        initListeners()
    }

    private fun setWindowInsets() {
        val view = requireActivity().findViewById<MaterialToolbar>(R.id.mt_booking_form_toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + padding,
                systemBars.top + padding,
                systemBars.right + padding,
                systemBars.bottom + padding
            )
            insets
        }
    }

    private fun overrideOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    callback.onCancelBooking()
                    isEnabled = false
                    super.handleOnBackCancelled()
                }
            })
    }

    private fun initListeners() {
        binding.btnBookingFormMakeBooking.setOnClickListener {
            callback.onMakeBooking(form)
        }

        binding.mtBookingFormToolbar.setNavigationOnClickListener {
            callback.onCancelBooking()
        }

        binding.btnBookingFormDatepicker.setOnClickListener {
            showDatePickerDialog()
        }

        binding.tietBookingFormNumTravelers.doAfterTextChanged { num ->
            var num: Int = num.toString().toIntOrNull() ?: minTravelers
            if (num < minTravelers) {
                num = minTravelers
                binding.tietBookingFormNumTravelers.setText(num.toString())
            }
            if (num > maxTravelers) {
                num = maxTravelers
                binding.tietBookingFormNumTravelers.setText(num.toString())
            }
            form.travelers = num
        }

        binding.btnBookingFromTravelersAdd.setOnClickListener {
            var num = binding.tietBookingFormNumTravelers.text.toString().toInt() + 1
            if (num > maxTravelers) num = maxTravelers
            binding.tietBookingFormNumTravelers.setText(num.toString())
        }

        binding.btnBookingFromTravelersRemove.setOnClickListener {
            var num = binding.tietBookingFormNumTravelers.text.toString().toInt() - 1
            if (num < minTravelers) num = minTravelers
            binding.tietBookingFormNumTravelers.setText(num.toString())
        }
    }

    /**
     * Display a range datePicker on screen.
     */
    private fun showDatePickerDialog() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select dates").setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            ).build()

        dateRangePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            var departureDate = selection.first ?: 0L
            var arrivalDate = selection.second ?: 0L
            if (departureDate != 0L && arrivalDate != 0L) {
                val formatDate = formatDate(departureDate, arrivalDate)
                binding.tvBookingFormDepartureDate.text = formatDate["departureDate"]
                binding.tvBookingFormArrivalDate.text = formatDate["arrivalDate"]
                form.departure = departureDate.toTimestamp()
                form.arrival = arrivalDate.toTimestamp()
            } else {
                Toast.makeText(
                    context, "Se deben seleccionar fechas de salida y llegada", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun formatDate(departureMillis: Long, arrivalMillis: Long): HashMap<String, String> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val departureDate = dateFormat.format(Date(departureMillis))
        val arrivalDate = dateFormat.format(Date(arrivalMillis))
        return hashMapOf(
            "departureDate" to departureDate, "arrivalDate" to arrivalDate
        )
    }

    fun Long.toTimestamp(): Timestamp {
        val milliseconds = this
        val seconds = milliseconds / 1000
        val nanoseconds = (milliseconds % 1000) * 1000000
        return Timestamp(seconds, nanoseconds.toInt())
    }

}