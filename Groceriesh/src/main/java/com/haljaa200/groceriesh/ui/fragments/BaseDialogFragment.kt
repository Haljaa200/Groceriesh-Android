package com.haljaa200.groceriesh.ui.fragments

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.RequestManager
import com.haljaa200.groceriesh.ui.MainActivity
import com.haljaa200.groceriesh.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseDialogFragment : DialogFragment() {
    lateinit var mainActivity: MainActivity
    lateinit var viewModel: MainViewModel
    @Inject lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (activity as MainActivity)
        viewModel = mainActivity.viewModel

    }
}