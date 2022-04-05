package com.haljaa200.groceriesh.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.FragmentProfileBinding
import com.haljaa200.groceriesh.util.Constants
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint


class ProfileFragment: BaseFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val layout = binding.root

        showMap()
        fillData()

        return layout
    }

    @SuppressLint("SetTextI18n")
    private fun fillData() {
        binding.tvFullName.text = "${viewModel.getString(Constants.USER_FIRST_NAME)} ${viewModel.getString(Constants.USER_LAST_NAME)}"
        binding.tvAddress.text = viewModel.getString(Constants.USER_DELIVERY_ADDRESS)
        binding.tvEmail.text = viewModel.getString(Constants.USER_EMAIL)
        binding.tvPhone.text = viewModel.getString(Constants.USER_PHONE)

        binding.ivEditProfile.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
        }

        binding.ivOrders.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToOrdersFragment())
        }

        binding.ivLogout.setOnClickListener {
            LogoutDialogFragment.openForResult(this, R.id.action_profileFragment_to_logoutDialogFragment) { confirmed ->
                if (confirmed) {
                    viewModel.removeUserData()
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHomeFragment())
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showMap() {
        val ctx = requireContext()
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        binding.map.setUseDataConnection(true)
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.setMultiTouchControls(false)
        binding.map.setBuiltInZoomControls(false)
        val mapController: IMapController = binding.map.controller
        mapController.setZoom(20)
        val defaultPoint = GeoPoint(viewModel.getString(Constants.USER_LATITUDE).toDouble(),viewModel.getString(Constants.USER_LONGITUDE).toDouble())
        mapController.setCenter(defaultPoint)

        binding.map.setOnTouchListener(OnTouchListener { _, arg1 ->
            if (arg1.action == MotionEvent.ACTION_UP) {
                return@OnTouchListener true
            }
            arg1.pointerCount <= 1
        })
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}