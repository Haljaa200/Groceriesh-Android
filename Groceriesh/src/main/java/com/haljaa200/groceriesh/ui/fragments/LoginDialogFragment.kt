package com.haljaa200.groceriesh.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.DialogLoginBinding
import com.haljaa200.groceriesh.models.Login
import com.haljaa200.groceriesh.util.Resource
import com.haljaa200.groceriesh.util.Tools
import com.haljaa200.groceriesh.util.Vlog

class LoginDialogFragment: BaseBottomSheetDialogFragment() {
    private var _binding: DialogLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loading: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLoginBinding.inflate(inflater, container, false)
        val layout = binding.root

        dialog?.setCancelable(false)
        loading = Tools.loadingDialog(requireContext())

        binding.btnRegister.setOnClickListener { findNavController().navigate(LoginDialogFragmentDirections.actionLoginDialogFragmentToRegisterDialogFragment()) }
        binding.btnLogin.setOnClickListener {
            if (isDataValid()) login()
        }

        viewModel.loggedIn.observe(viewLifecycleOwner) {
            if (!it) findNavController().navigateUp()
        }

        return layout
    }

    private fun isDataValid(): Boolean {
        return when {

            binding.etEmail.text.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etEmail)
                false
            }

            binding.etPassword.text.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etPassword)
                false
            }

            else -> true
        }
    }

    private fun login() {
        viewModel.login(Login(binding.etEmail.text.toString(), binding.etPassword.text.toString()))
        viewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let {
                        if (it.success) {
                            viewModel.saveUserData(it.data.customer!!, it.data.token)
                            viewModel.loggedIn.postValue(true)
                            findNavController().navigateUp()
                        } else {
                            Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is Resource.Error -> {
                    loading.dismiss()

                    response.message?.let { message: String ->
                        Vlog.e(message)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    Vlog.i("Loading...")
                    loading.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}