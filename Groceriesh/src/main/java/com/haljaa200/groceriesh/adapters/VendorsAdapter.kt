package com.haljaa200.groceriesh.adapters

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.haljaa200.groceriesh.databinding.RvItemVendorBinding
import com.haljaa200.groceriesh.models.Vendor
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint

class VendorsAdapter : RecyclerView.Adapter<VendorsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RvItemVendorBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun bind(vendor: Vendor) {

            with(binding) {
                val context = this.item.context

                binding.tvStoreName.text = vendor.store_name
                binding.tvStoreAddress.text = vendor.address
                Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
                Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
                binding.map.setUseDataConnection(true)
                binding.map.setTileSource(TileSourceFactory.MAPNIK)
                binding.map.setMultiTouchControls(false)
                binding.map.setBuiltInZoomControls(false)
                val mapController: IMapController = binding.map.controller
                mapController.setZoom(20)
                val defaultPoint = GeoPoint(vendor.latitude, vendor.longitude)
                mapController.setCenter(defaultPoint)

                binding.map.setOnTouchListener(View.OnTouchListener { _, arg1 ->
                    if (arg1.action == MotionEvent.ACTION_UP) {
                        return@OnTouchListener true
                    }
                    arg1.pointerCount <= 1
                })

                this.item.setOnClickListener {
                    onClickListener?.let { it(vendor) }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(differ.currentList[position])

    private val differCallback = object: DiffUtil.ItemCallback<Vendor>() {
        override fun areItemsTheSame(oldItem: Vendor, newItem: Vendor): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Vendor, newItem: Vendor): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemVendorBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onClickListener: ((Vendor) -> Unit)? = null
    fun setOnClickListener(listener: ((Vendor) -> Unit)) {
        onClickListener = listener
    }

}