package com.haljaa200.groceriesh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.haljaa200.groceriesh.databinding.DialogLoginBinding
import com.haljaa200.groceriesh.databinding.DialogRegisterBinding

class RegisterDialogFragment: BaseBottomSheetDialogFragment() {
    private var _binding: DialogRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRegisterBinding.inflate(inflater, container, false)
        val layout = binding.root

        dialog?.setCancelable(false)
        binding.btnBacktoLogin.setOnClickListener { findNavController().navigateUp() }

        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}