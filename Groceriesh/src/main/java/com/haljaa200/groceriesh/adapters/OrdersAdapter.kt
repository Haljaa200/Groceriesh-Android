package com.haljaa200.groceriesh.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.size
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.RvItemOrderBinding
import com.haljaa200.groceriesh.models.OrdersResponseObject

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RvItemOrderBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        fun bind(order: OrdersResponseObject) {

            with(binding) {
                val context = binding.item.context

                if (order.delivery_time == 0L) {
                    binding.tvDelivered.text = context.getString(R.string.not_delivered)
                    binding.tvDelivered.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_cross, 0, 0, 0)
                } else {
                    binding.tvDelivered.text = context.getString(R.string.delivered)
                    binding.tvDelivered.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                }

                binding.tvPrice.text = "${order.total_price}${context.getString(R.string.priceUnit)}"
                binding.tvDeliveryAddress.text = order.delivery_address
                var items = ""
                order.items.forEach {
                    items += "${it.name}(${it.quantity}), "
                }
                items = items.dropLast(2)

                binding.tvItems.text = items

                this.item.setOnClickListener {
                    onClickListener?.let { it(order) }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(differ.currentList[position])

    private val differCallback = object: DiffUtil.ItemCallback<OrdersResponseObject>() {
        override fun areItemsTheSame(oldItem: OrdersResponseObject, newItem: OrdersResponseObject): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: OrdersResponseObject, newItem: OrdersResponseObject): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemOrderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onClickListener: ((OrdersResponseObject) -> Unit)? = null
    fun setOnClickListener(listener: ((OrdersResponseObject) -> Unit)) {
        onClickListener = listener
    }
}