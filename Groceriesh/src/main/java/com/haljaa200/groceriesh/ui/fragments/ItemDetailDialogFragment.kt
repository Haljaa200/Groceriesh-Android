package com.haljaa200.groceriesh.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.DialogItemDetailBinding
import com.haljaa200.groceriesh.models.OrderItem

class ItemDetailDialogFragment: BaseBottomSheetDialogFragment() {
    private var _binding: DialogItemDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ItemDetailDialogFragmentArgs by navArgs()
    var count = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogItemDetailBinding.inflate(inflater, container, false)
        val layout = binding.root

        glide.load(args.item.photo).placeholder(R.drawable.ic_grocery).error(R.drawable.ic_grocery).into(binding.ivPhoto)
        binding.tvPrice.text = "${args.item.price}${resources.getString(R.string.priceUnit)}"
        binding.tvName.text = args.item.name
        binding.tvDescription.text = args.item.description
        viewModel.getOrderItem(args.item._id).observe(viewLifecycleOwner) {
            it?.let {
                if (it.quantity > 0) {
                    count = it.quantity
                    binding.btnAddToBasket.visibility = View.GONE
                    binding.countLayout.visibility = View.VISIBLE
                    binding.tvCount.text = it.quantity.toString()
                }
            }
        }

        binding.btnAddToBasket.setOnClickListener {
            count += 1
            binding.tvCount.text = count.toString()
            viewModel.saveOrderItem(OrderItem(null, args.item._id, args.item.vendor_id, args.item.name, args.item.photo.toString(), args.item.price, count, args.item.unit))

            it.visibility = View.GONE
            binding.countLayout.visibility = View.VISIBLE
        }

        binding.ivMinus.setOnClickListener {
            count -= 1
            binding.tvCount.text = count.toString()
            if (count == 0) {
                binding.countLayout.visibility = View.GONE
                binding.btnAddToBasket.visibility = View.VISIBLE
                viewModel.deleteOrderItem(args.item._id)
            } else {
                viewModel.updateOrderItemQuantity(args.item._id, count)
            }
        }

        binding.ivPlus.setOnClickListener {
            count += 1
            viewModel.updateOrderItemQuantity(args.item._id, count)
            binding.tvCount.text = count.toString()
        }

        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}