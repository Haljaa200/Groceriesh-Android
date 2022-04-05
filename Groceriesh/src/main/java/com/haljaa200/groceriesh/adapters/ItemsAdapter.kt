package com.haljaa200.groceriesh.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.RvItemProductBinding
import com.haljaa200.groceriesh.models.Item

class ItemsAdapter(val glide: RequestManager) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RvItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
        fun bind(item: Item) {

            with(binding) {
                val resources = this.ivItem.resources

                glide.load(item.photo).placeholder(R.drawable.ic_grocery).error(R.drawable.ic_grocery).into(this.ivItem)
                binding.tvPrice.text = "${item.price}${resources.getString(R.string.priceUnit)}"
                binding.tvName.text = item.name

                this.ivItem.setOnClickListener {
                    onClickListener?.let { it(item) }
                }

            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(differ.currentList[position])

    private val differCallback = object: DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onClickListener: ((Item) -> Unit)? = null

    fun setOnClickListener(listener: ((Item) -> Unit)) {
        onClickListener = listener
    }
}