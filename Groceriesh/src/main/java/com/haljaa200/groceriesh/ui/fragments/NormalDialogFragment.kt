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
import androidx.navigation.fragment.navArgs
import com.haljaa200.groceriesh.databinding.DialogNormalBinding
import com.haljaa200.groceriesh.models.DialogInfo
import com.haljaa200.groceriesh.util.Constants.DIALOG_RESULT
import com.haljaa200.groceriesh.util.Tools

class NormalDialogFragment: BaseBottomSheetDialogFragment() {
    private var _binding: DialogNormalBinding? = null
    private val binding get() = _binding!!
    private val args: NormalDialogFragmentArgs by navArgs()

    companion object {
        fun openForResult(fragment: Fragment, action: Int, dialogInfo: DialogInfo, onResult: (Boolean) -> Unit) {
            fragment.setFragmentResultListener(DIALOG_RESULT) { _, bundle ->
                val confirmed = bundle[DIALOG_RESULT] as Boolean
                onResult(confirmed)
            }

            fragment.findNavController().navigate(action, NormalDialogFragmentArgs(dialogInfo).toBundle())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNormalBinding.inflate(inflater, container, false)
        val layout = binding.root

        binding.tvTitle.text = resources.getString(args.dialogInfo.title)
        binding.tvtext.text = resources.getString(args.dialogInfo.text)

        binding.btnOk.setTextColor(Tools.getColorStateList(requireContext(), args.dialogInfo.okTextColor))

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
            setFragmentResult(DIALOG_RESULT, bundleOf(DIALOG_RESULT to false))
        }

        binding.btnOk.setOnClickListener {
            findNavController().navigateUp()
            setFragmentResult(DIALOG_RESULT, bundleOf(DIALOG_RESULT to true))
        }

        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}