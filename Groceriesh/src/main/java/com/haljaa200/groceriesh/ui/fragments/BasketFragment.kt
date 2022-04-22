package com.haljaa200.groceriesh.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.adapters.BasketAdapter
import com.haljaa200.groceriesh.databinding.FragmentBasketBinding
import com.haljaa200.groceriesh.models.DefaultOrderItem
import com.haljaa200.groceriesh.models.DialogInfo
import com.haljaa200.groceriesh.models.Order
import com.haljaa200.groceriesh.util.Constants

class BasketFragment: BaseFragment() {
    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvBasketAdapter: BasketAdapter
    private var vendorId = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        val layout = binding.root

        setupToolbar(binding.toolbar, true)
        setupRecyclerView()
        setupBottomInfo()

        return layout
    }

    private fun setupRecyclerView() {
        rvBasketAdapter = BasketAdapter(glide)

        binding.rvBasket.apply {
            adapter = rvBasketAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        
        viewModel.getBasketOrders().observe(viewLifecycleOwner) {
            if (it.isEmpty())
                findNavController().navigateUp()
            else
                rvBasketAdapter.differ.submitList(it.toMutableList())
        }

        rvBasketAdapter.setOnMinusClickListener {
            if (it.quantity == 1) viewModel.deleteOrderItem(it._id)
            else viewModel.updateOrderItemQuantity(it._id, it.quantity - 1)
        }

        rvBasketAdapter.setOnPlusClickListener {
            viewModel.updateOrderItemQuantity(it._id, it.quantity + 1)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupBottomInfo() {
        viewModel.getBasketSum().observe(viewLifecycleOwner) {
            binding.tvTotal.text = "${getString(R.string.total)}: ${it.toString().replace("null", "0")}${resources.getString(R.string.priceUnit)}"
        }

        binding.btnDeleteBasket.setOnClickListener {
            val info = DialogInfo(R.string.empty_basket, R.string.empty_basket_prompt, R.color.error)

            NormalDialogFragment.openForResult(this, R.id.action_basketFragment_to_normalDialogFragment, info) { confirmed ->
                if (confirmed) {
                    viewModel.deleteBasket()
                    findNavController().navigateUp()
                }
            }
        }

        binding.btnContinue.setOnClickListener {
            val items = mutableListOf<DefaultOrderItem>()
            rvBasketAdapter.differ.currentList.forEach {
                vendorId = it.vendor_id
                items.add(DefaultOrderItem(it.name, it.price, it.quantity, it.unit))
            }
            val order = Order(
                vendor_id = vendorId,
                customer_id = viewModel.getString(Constants.USER_ID),
                delivery_address = viewModel.getString(Constants.USER_DELIVERY_ADDRESS),
                delivery_latitude = viewModel.getString(Constants.USER_LATITUDE).toDouble(),
                delivery_longitude = viewModel.getString(Constants.USER_LONGITUDE).toDouble(),
                delivered = false,
                delivery_time_planned = 0,
                items = items,
                notes = "",
                total_price = binding.tvTotal.text.toString().replace("${getString(R.string.total)}: ", "").replace(resources.getString(R.string.priceUnit), "").toDouble()
            )

            findNavController().navigate(BasketFragmentDirections.actionBasketFragmentToCheckoutFragment(order))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}