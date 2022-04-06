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
import com.haljaa200.groceriesh.models.DialogInfo

class BasketFragment: BaseFragment() {
    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvBasketAdapter: BasketAdapter

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

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}