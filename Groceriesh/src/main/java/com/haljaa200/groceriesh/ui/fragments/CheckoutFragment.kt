package com.haljaa200.groceriesh.ui.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.FragmentCheckoutBinding
import com.haljaa200.groceriesh.util.Resource
import com.haljaa200.groceriesh.util.Vlog
import java.util.*

class CheckoutFragment: BaseFragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val args: CheckoutFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        val layout = binding.root

        setupToolbar(binding.toolbar, true)
        setupBottomInfo()

        binding.tvDeliveryTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(hour)
                    .setMinute(minute)
                    .setTitleText("Select Delivery time")
                    .build()

            picker.addOnPositiveButtonClickListener {

                val formattedTime: String = when {
                    picker.hour == 0 -> {
                        if (picker.minute < 10) {
                            "${picker.hour + 12}:0${picker.minute} am"
                        } else {
                            "${picker.hour + 12}:${picker.minute} am"
                        }
                    }
                    picker.hour > 12 -> {
                        if (picker.minute < 10) {
                            "${picker.hour - 12}:0${picker.minute} pm"
                        } else {
                            "${picker.hour - 12}:${picker.minute} pm"
                        }
                    }
                    picker.hour == 12 -> {
                        if (picker.minute < 10) {
                            "${picker.hour}:0${picker.minute} pm"
                        } else {
                            "${picker.hour}:${picker.minute} pm"
                        }
                    }
                    else -> {
                        if (picker.minute < 10) {
                            "${picker.hour}:${picker.minute} am"
                        } else {
                            "${picker.hour}:${picker.minute} am"
                        }
                    }
                }

                binding.tvDeliveryTime.text = formattedTime
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, picker.hour)
                calendar.set(Calendar.MINUTE, picker.minute)
                args.order.delivery_time_planned = calendar.timeInMillis
            }

            picker.show(parentFragmentManager, "TIME_PICKER")
        }

        return layout
    }

    @SuppressLint("SetTextI18n")
    private fun setupBottomInfo() {
        viewModel.getBasketSum().observe(viewLifecycleOwner) {
            binding.tvTotal.text = "${getString(R.string.total)}: ${it.toString().replace("null", "0")}${resources.getString(R.string.priceUnit)}"
        }

        binding.tvAddress.text = getString(R.string.delivery_to_address).replace("\$ADDRESS", args.order.delivery_address)

        binding.btnContinue.setOnClickListener {
            var notes = binding.etNote.text.toString()
            if (binding.chDontRingTheBell.isChecked) notes += "\n" + binding.chDontRingTheBell.text
            if (binding.chLeaveOrderAtDoor.isChecked) notes += "\n" + binding.chLeaveOrderAtDoor.text

            args.order.notes = notes

            if (binding.tvDeliveryTime.text == getString(R.string.choose_delivery_time))
                Toast.makeText(requireContext(), getString(R.string.please_choose_delivery_time), Toast.LENGTH_SHORT).show()
            else
                submitOrder()
        }
    }

    private fun submitOrder() {
        viewModel.submitOrder(args.order)
        viewModel.submitOrderResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()

                    response.data?.let {
                        if (it.success) {
                            viewModel.deleteBasket()
                            Toast.makeText(requireContext(), "Order Submitted.", Toast.LENGTH_SHORT).show()
                            findNavController().navigateUp()
                        } else {
                            Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is Resource.Error -> {
                    loading.dismiss()
                    response.message?.let { message: String ->
                        Vlog.e(message)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    loading.show()
                    Vlog.i("Loading...")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}