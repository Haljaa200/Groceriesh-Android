package com.haljaa200.groceriesh.ui.fragments

import android.os.Bundle
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.haljaa200.groceriesh.databinding.FragmentHomeBinding
import com.haljaa200.groceriesh.util.Constants

class HomeFragment: BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val layout = binding.root

        if (viewModel.getBoolean(Constants.LOGGED_IN)) showData()
        else showLogin()

        return layout
    }

    private fun showLogin() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginDialogFragment())
    }

    private fun showData() {
        //TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}