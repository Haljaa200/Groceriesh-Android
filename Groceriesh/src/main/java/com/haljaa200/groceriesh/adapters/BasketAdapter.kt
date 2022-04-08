package com.haljaa200.groceriesh.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.RvItemBasketBinding
import com.haljaa200.groceriesh.models.OrderItem

class BasketAdapter(val glide: RequestManager, val showPlusMinus: Boolean = true) : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RvItemBasketBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        fun bind(orderItem: OrderItem) {

            with(binding) {
                val resources = this.ivItem.resources

                glide.load(orderItem.photo).placeholder(R.drawable.ic_grocery).error(R.drawable.ic_grocery).into(this.ivItem)
                binding.tvName.text = orderItem.name
                binding.tvPrice.text = "${orderItem.price}${resources.getString(R.string.priceUnit)}"
                binding.tvUnit.text = orderItem.unit
                binding.tvCount.text = orderItem.quantity.toString()

                if (showPlusMinus) {
                    this.ivPlus.visibility = View.VISIBLE
                    this.ivMinus.visibility = View.VISIBLE

                } else {
                    this.ivPlus.visibility = View.GONE
                    this.ivMinus.visibility = View.GONE
                }

                this.ivPlus.setOnClickListener {
                    onPlusClickListener?.let { it(orderItem) }
                }

                this.ivMinus.setOnClickListener {
                    onMinusClickListener?.let { it(orderItem) }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(differ.currentList[position])

    private val differCallback = object: DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemBasketBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onPlusClickListener: ((OrderItem) -> Unit)? = null
    fun setOnPlusClickListener(listener: ((OrderItem) -> Unit)) {
        onPlusClickListener = listener
    }

    private var onMinusClickListener: ((OrderItem) -> Unit)? = null
    fun setOnMinusClickListener(listener: ((OrderItem) -> Unit)) {
        onMinusClickListener = listener
    }
}