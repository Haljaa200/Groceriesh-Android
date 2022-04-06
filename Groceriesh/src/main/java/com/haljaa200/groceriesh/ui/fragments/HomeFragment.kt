package com.haljaa200.groceriesh.ui.fragments

import android.R.attr.buttonStyle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.adapters.ItemsAdapter
import com.haljaa200.groceriesh.databinding.FragmentHomeBinding
import com.haljaa200.groceriesh.util.Constants
import com.haljaa200.groceriesh.util.Resource
import com.haljaa200.groceriesh.util.Vlog


class HomeFragment: BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvItemsAdapter: ItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val layout = binding.root

        viewModel.loggedIn.observe(viewLifecycleOwner) {
            Vlog.i("loggedIn", it.toString())
            if (it) showData()
            else showLogin()
        }

        return layout
    }

    private fun showLogin() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginDialogFragment())
    }

    private fun showData() {
        binding.tvAddress.text = viewModel.getString(Constants.USER_DELIVERY_ADDRESS)
        binding.chNewProducts.visibility = View.VISIBLE
        setupRecyclerView()
        showCategories()
        showItems()
    }

    private fun setupRecyclerView() {
        rvItemsAdapter = ItemsAdapter(glide)

        binding.rvItems.apply {
            adapter = rvItemsAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun showItems() {
        viewModel.getItems()
        viewModel.itemsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        if (it.success) {
                            rvItemsAdapter.differ.submitList(it.data.items.toMutableList())
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

    private fun showCategories() {
        binding.chNewProducts.setOnCheckedChangeListener { chip, checked ->
            chip.isClickable = !checked
        }

        binding.chNewProducts.setOnClickListener {
            viewModel.getItems()
        }

        viewModel.getCategories()
        viewModel.categoriesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        if (it.success) {
                            it.data.categories.forEach { category ->
                                val chip = LayoutInflater.from(requireContext()).inflate(R.layout.chip, null) as Chip
                                chip.setOnCheckedChangeListener { chip, checked ->
                                    chip.isClickable = !checked
                                }
                                chip.text = category.name
                                chip.setOnClickListener {
                                    viewModel.getCategoryItems(category._id)
                                }
                                binding.chipGroupCategories.addView(chip)
                            }
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