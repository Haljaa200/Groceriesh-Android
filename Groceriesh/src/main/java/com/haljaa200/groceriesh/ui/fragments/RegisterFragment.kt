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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.haljaa200.groceriesh.databinding.FragmentRegisterBinding
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

        return layout
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
        val startPoint = GeoPoint(55.8668183,-4.2499602)

        mapController.setCenter(startPoint)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}