package com.haljaa200.groceriesh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.haljaa200.groceriesh.databinding.FragmentHomeBinding
import com.haljaa200.groceriesh.util.Constants
import com.haljaa200.groceriesh.util.Vlog

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}