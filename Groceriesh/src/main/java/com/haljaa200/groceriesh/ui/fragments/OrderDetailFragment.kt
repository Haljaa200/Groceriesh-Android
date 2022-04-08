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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.adapters.BasketAdapter
import com.haljaa200.groceriesh.databinding.FragmentOrderDetailBinding
import com.haljaa200.groceriesh.models.OrderItem
import com.haljaa200.groceriesh.models.SingleOrderResponse
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
import java.util.*

class OrderDetailFragment: BaseFragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!
    private val args: OrderDetailFragmentArgs by navArgs()
    private lateinit var rvBasketAdapter: BasketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        val layout = binding.root

        setupToolbar(binding.toolbar, true)
        showMap()
        setupRecyclerView()
        getData()

        return layout
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
        mapController.setZoom(17)
        val defaultPoint = GeoPoint(viewModel.getString(Constants.USER_LATITUDE).toDouble(),viewModel.getString(
            Constants.USER_LONGITUDE).toDouble())
        mapController.setCenter(defaultPoint)
    }

    private fun setupRecyclerView() {
        rvBasketAdapter = BasketAdapter(glide, false)

        binding.rvBasket.apply {
            adapter = rvBasketAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getData() {
        viewModel.getOrder(args.orderId)
        viewModel.singleOrderResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    loading.dismiss()
                    response.data?.let {
                        if (it.success) {
                            showData(it)
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

    @SuppressLint("SetTextI18n")
    private fun showData(orderResponse: SingleOrderResponse) {
        val order = orderResponse.data.order
        addMyMarker(order.delivery_latitude, order.delivery_longitude)
        binding.tvDeliveryTime.text = getString(R.string.delivery_time_placeholder).replace("\$TIME", Tools.getFormatTimeWithTZ(Date(order.delivery_time_planned)).toString())
        if (order.delivery_time == 0L) {
            binding.tvDeliveredAt.text = getString(R.string.delivered_time_placeholder).replace("\$TIME", "-")
        } else {
            binding.tvDeliveredAt.text = getString(R.string.delivered_time_placeholder).replace("\$TIME", Tools.getFormatTimeWithTZ(Date(order.delivery_time)).toString())
        }

        binding.tvNotes.text = getString(R.string.notes_placeholder).replace("\$NOTES", order.notes)
        val items = mutableListOf<OrderItem>()
        order.items.forEach { items.add(OrderItem(null, it._id, it.name, it.photo.toString().replace("null", ""), it.price, it.quantity, it.unit)) }
        rvBasketAdapter.differ.submitList(items)

        binding.tvTotal.text = "${getString(R.string.total)}: ${order.total_price}${resources.getString(R.string.priceUnit)}"
        binding.tvAddress.text = getString(R.string.delivery_to_address).replace("\$ADDRESS", order.delivery_address)
    }


    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    private fun addMyMarker(latitude: Double, longitude: Double) {
        val marker = Marker(binding.map)
        marker.position = GeoPoint(latitude, longitude)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}