package com.haljaa200.groceriesh.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.haljaa200.groceriesh.databinding.ToolbarBinding
import com.haljaa200.groceriesh.ui.MainActivity
import com.haljaa200.groceriesh.ui.MainViewModel
import com.haljaa200.groceriesh.util.Tools
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var viewModel: MainViewModel
    @Inject lateinit var glide: RequestManager
    lateinit var loading: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (activity as MainActivity)
        viewModel = mainActivity.viewModel

        loading = Tools.loadingDialog(requireContext())
    }

    fun setupToolbar(toolbar: ToolbarBinding, showBackButton: Boolean) {
        if (showBackButton) toolbar.ivBack.visibility = View.VISIBLE
        else toolbar.ivBack.visibility = View.GONE

        toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}