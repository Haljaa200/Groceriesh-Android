package com.haljaa200.groceriesh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haljaa200.groceriesh.databinding.FragmentOrdersBinding

class OrdersFragment: BaseFragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val layout = binding.root

        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}