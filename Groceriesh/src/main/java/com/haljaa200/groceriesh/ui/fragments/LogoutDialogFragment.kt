package com.haljaa200.groceriesh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.haljaa200.groceriesh.databinding.DialogLogoutBinding
import com.haljaa200.groceriesh.util.Constants.LOGOUT_DIALOG_RESULT

class LogoutDialogFragment: BaseBottomSheetDialogFragment() {
    private var _binding: DialogLogoutBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun openForResult(fragment: Fragment, action: Int, onResult: (Boolean) -> Unit) {
            fragment.setFragmentResultListener(LOGOUT_DIALOG_RESULT) { _, bundle ->
                val confirmed = bundle[LOGOUT_DIALOG_RESULT] as Boolean
                onResult(confirmed)
            }

            fragment.findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLogoutBinding.inflate(inflater, container, false)
        val layout = binding.root

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
            setFragmentResult(LOGOUT_DIALOG_RESULT, bundleOf(LOGOUT_DIALOG_RESULT to false))
        }

        binding.btnOk.setOnClickListener {
            findNavController().navigateUp()
            setFragmentResult(LOGOUT_DIALOG_RESULT, bundleOf(LOGOUT_DIALOG_RESULT to true))
        }

        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}