package com.haljaa200.groceriesh.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.FragmentRegisterBinding
import com.haljaa200.groceriesh.models.Register
import com.haljaa200.groceriesh.util.Resource
import com.haljaa200.groceriesh.util.Tools
import com.haljaa200.groceriesh.util.Vlog
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class RegisterFragment: BaseFragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val REQUEST_LOCATION_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val layout = binding.root
        setupToolbar(binding.toolbar, true)

        showMap()
        binding.btnRegister.setOnClickListener {
            if (isDataValid()) register()
        }

        return layout
    }

    private fun register() {
        val registerData = Register(
            binding.etAddress.text.toString(),
            binding.etEmail.text.toString(),
            binding.etFirstName.text.toString(),
            binding.etLastName.text.toString(),
            binding.mapContainer.map.mapCenter.latitude,
            binding.mapContainer.map.mapCenter.longitude,
            binding.etPassword.text.toString(),
            binding.etPhone.text.toString()
        )

        viewModel.register(registerData)
        viewModel.registerResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let {
                        if (it.success) {
                            viewModel.saveUserData(it.data.customer!!, it.data.token)
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

    private fun showMap() {
        val ctx = requireContext()
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        binding.mapContainer.map.setUseDataConnection(true)
        binding.mapContainer.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapContainer.map.setBuiltInZoomControls(true)
        binding.mapContainer.map.setMultiTouchControls(true)
        val mapController: IMapController = binding.mapContainer.map.controller
        mapController.setZoom(19)
        val defaultPoint = GeoPoint(55.8668183,-4.2499602)
        mapController.setCenter(defaultPoint)
        initMyLocation()

        binding.mapContainer.ivMyLocation.setOnClickListener { initMyLocation() }

        handleMapTouches()
    }

    private fun initMyLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            } else {
                checkLocationPermission()
            }
        } else {
            enableMyLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        val mGpsMyLocationProvider = GpsMyLocationProvider(activity)
        mGpsMyLocationProvider.addLocationSource(LocationManager.NETWORK_PROVIDER)
        val mLocationOverlay = MyLocationNewOverlay(mGpsMyLocationProvider, binding.mapContainer.map)
        mLocationOverlay.enableMyLocation()
        mLocationOverlay.enableFollowLocation()
        binding.mapContainer.map.overlays.add(mLocationOverlay)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
            } else ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
        }
    }

    private fun handleMapTouches() {
        val touchListener: (v: View, event: MotionEvent) -> Boolean = { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                MotionEvent.ACTION_UP -> {
                    binding.scrollView.requestDisallowInterceptTouchEvent(false)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    binding.scrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        }
        binding.mapContainer.transparentImage.setOnTouchListener(View.OnTouchListener(touchListener))
    }

    override fun onResume() {
        super.onResume()
        binding.mapContainer.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapContainer.map.onPause()
    }

    private fun isDataValid(): Boolean {
        return when {
            binding.etFirstName.text.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.enter_name), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etFirstName)
                false
            }
            binding.etLastName.text.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.enter_last_name), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etLastName)
                false
            }
            binding.etEmail.text.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etEmail)
                false
            }
            !Tools.isEmailValid(binding.etEmail.text.toString()) -> {
                Toast.makeText(requireContext(), getString(R.string.email_not_valid), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etEmail)
                false
            }
            binding.etPhone.text.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.enter_phone), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etPhone)
                false
            }
            binding.etPassword.text.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etPassword)
                false
            }
            binding.etPasswordConfirm.text.isEmpty() -> {
                Toast.makeText(requireContext(), getString(R.string.enter_password_confirm), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etPasswordConfirm)
                false
            }
            binding.etPassword.text.toString() != binding.etPasswordConfirm.text.toString() -> {
                Toast.makeText(requireContext(), getString(R.string.password_and_confirm_not_same), Toast.LENGTH_SHORT).show()
                Tools.shakeView(binding.etPassword)
                Tools.shakeView(binding.etPasswordConfirm)
                false
            }

            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}