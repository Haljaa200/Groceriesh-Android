package com.haljaa200.groceriesh.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.adapters.ItemsAdapter
import com.haljaa200.groceriesh.databinding.FragmentHomeBinding
import com.haljaa200.groceriesh.models.Vendor
import com.haljaa200.groceriesh.util.Constants
import com.haljaa200.groceriesh.util.Resource
import com.haljaa200.groceriesh.util.Tools
import com.haljaa200.groceriesh.util.Vlog
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM
import org.osmdroid.views.overlay.Marker.ANCHOR_CENTER
import java.lang.Exception

class HomeFragment: BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvItemsAdapter: ItemsAdapter
    private val vendors = mutableListOf<Vendor>()
    private var selectedVendorId = ""

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
        binding.chNewProducts.visibility = View.VISIBLE
        setupItemsRecyclerView()
        showBasket()
        showMap()
        showVendors()
        showCategories()
        showItems()
    }

    @SuppressLint("SetTextI18n")
    private fun showBasket() {
        viewModel.getBasketSum().observe(viewLifecycleOwner) {
            if (it == 0.0 || it == null) binding.tvBasket.visibility = View.GONE
            else {
                binding.tvBasket.visibility = View.VISIBLE
                binding.tvBasket.text = "${it.toString().replace("null", "0")}${resources.getString(R.string.priceUnit)}"
            }
        }

        binding.tvBasket.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBasketFragment())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showMap() {
        binding.map.visibility = View.VISIBLE
        val ctx = requireContext()
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        binding.map.setUseDataConnection(true)
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.setMultiTouchControls(false)
        binding.map.setBuiltInZoomControls(false)
        val mapController: IMapController = binding.map.controller
        mapController.setZoom(17)
        try {
            val defaultPoint = GeoPoint(viewModel.getString(Constants.USER_LATITUDE).toDouble(),viewModel.getString(Constants.USER_LONGITUDE).toDouble())
            mapController.setCenter(defaultPoint)
            addMyMarker()
        } catch (e: Exception) {}
    }

    private fun addMyMarker() {
        val marker = Marker(binding.map)
        marker.position = GeoPoint(viewModel.getString(Constants.USER_LATITUDE).toDouble(),viewModel.getString(Constants.USER_LONGITUDE).toDouble())
        marker.setAnchor(ANCHOR_CENTER, ANCHOR_BOTTOM)
        marker.title = getString(R.string.my_location)
        val bitmap = Tools.getBitmapFromVector(requireContext(), R.drawable.ic_location)
        val dr: Drawable = BitmapDrawable(
            resources, Bitmap.createScaledBitmap(
                bitmap,
                (48.0f * resources.displayMetrics.density).toInt(),
                (48.0f * resources.displayMetrics.density).toInt(), true
            )
        )
        marker.icon = dr
        binding.map.overlays.add(marker)
        binding.map.invalidate()
    }

    private fun addVendorMarker(vendor: Vendor) {
        val marker = Marker(binding.map)
        marker.position = GeoPoint(vendor.latitude, vendor.longitude)
        marker.setAnchor(ANCHOR_CENTER, ANCHOR_BOTTOM)
        marker.title = vendor.store_name
        val bitmap = Tools.getBitmapFromVector(requireContext(), R.drawable.ic_store)
        val dr: Drawable = BitmapDrawable(
            resources, Bitmap.createScaledBitmap(
                bitmap,
                (35.0f * resources.displayMetrics.density).toInt(),
                (31.0f * resources.displayMetrics.density).toInt(), true
            )
        )
        marker.icon = dr
        marker.setOnMarkerClickListener { clickedMarker, _ ->
            val selectedVendor = vendors.first { it.store_name == clickedMarker.title && it.latitude == clickedMarker.position.latitude && it.longitude == clickedMarker.position.longitude }
            selectedVendorId = selectedVendor._id
            viewModel.getVendorItems(selectedVendor._id)
            binding.chNewProducts.isChecked = true
            binding.filterLayout.visibility = View.VISIBLE
            binding.tvFilter.text = getString(R.string.filter_prompt).replace("\$NAME", clickedMarker.title)

            binding.ivRemoveFilter.setOnClickListener {
                binding.filterLayout.visibility = View.GONE
                binding.chNewProducts.isChecked = true
                selectedVendorId = ""
                viewModel.getItems()
            }

            true
        }
        binding.map.overlays.add(marker)
        binding.map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    private fun setupItemsRecyclerView() {
        rvItemsAdapter = ItemsAdapter(glide)

        binding.rvItems.apply {
            adapter = rvItemsAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        rvItemsAdapter.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToItemDetailDialogFragment(it))
        }
    }

    private fun showVendors() {
        viewModel.getVendors()
        viewModel.vendorsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        if (it.success) {
                            vendors.clear()
                            vendors.addAll(it.data.vendors)
                            it.data.vendors.forEach { vendor ->
                                addVendorMarker(vendor)
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message: String ->
                        Vlog.e(message)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    Vlog.i("Loading...")
                }
            }
        }
    }

    private fun showItems() {
        viewModel.getItems()
        viewModel.itemsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        if (it.success) {
                            rvItemsAdapter.differ.submitList(it.data.items.toMutableList())
                        }
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message: String ->
                        Vlog.e(message)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    Vlog.i("Loading...")
                }
            }
        }
    }

    private fun showCategories() {
        binding.chNewProducts.setOnCheckedChangeListener { chip, checked ->
            chip.isClickable = !checked
        }

        binding.chNewProducts.setOnClickListener {
            viewModel.getItems()
        }

        viewModel.getCategories()
        viewModel.categoriesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        if (it.success) {
                            if (binding.chipGroupCategories.childCount > 1) return@observe

                            it.data.categories.forEach { category ->
                                val chip = LayoutInflater.from(requireContext()).inflate(R.layout.chip, null) as Chip
                                chip.setOnCheckedChangeListener { chip, checked ->
                                    chip.isClickable = !checked
                                }
                                chip.text = category.name
                                chip.setOnClickListener {
                                    if (selectedVendorId.isEmpty()) viewModel.getCategoryItems(category._id)
                                    else viewModel.getVendorItems(selectedVendorId, category._id)
                                }
                                binding.chipGroupCategories.addView(chip)
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message: String ->
                        Vlog.e(message)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    Vlog.i("Loading...")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}