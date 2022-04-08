package com.haljaa200.groceriesh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.haljaa200.groceriesh.adapters.OrdersAdapter
import com.haljaa200.groceriesh.databinding.FragmentOrdersBinding
import com.haljaa200.groceriesh.util.Resource
import com.haljaa200.groceriesh.util.Vlog

class OrdersFragment: BaseFragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvOrdersAdapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val layout = binding.root

        setupToolbar(binding.toolbar, false)
        setupRecyclerView()
        getData()

        return layout
    }

    private fun setupRecyclerView() {
        rvOrdersAdapter = OrdersAdapter()

        binding.rvOrders.apply {
            adapter = rvOrdersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        rvOrdersAdapter.setOnClickListener {
            findNavController().navigate(OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(it._id))
        }
    }

    private fun getData() {
        viewModel.getOrders()
        viewModel.ordersResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        if (it.success) {
                            rvOrdersAdapter.differ.submitList(it.data.orders.reversed())
                        }
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message: String ->
                        Vlog.e(message)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
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