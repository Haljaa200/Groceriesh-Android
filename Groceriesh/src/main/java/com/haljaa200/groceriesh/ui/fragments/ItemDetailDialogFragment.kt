package com.haljaa200.groceriesh.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.DialogItemDetailBinding

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

        binding.btnAddToBasket.setOnClickListener {
            count += 1
            binding.tvCount.text = count.toString()
            it.visibility = View.GONE
            binding.countLayout.visibility = View.VISIBLE
        }

        binding.ivMinus.setOnClickListener {
            count -= 1
            binding.tvCount.text = count.toString()
            if (count == 0) {
                binding.countLayout.visibility = View.GONE
                binding.btnAddToBasket.visibility = View.VISIBLE
            }
        }

        binding.ivPlus.setOnClickListener {
            count += 1
            binding.tvCount.text = count.toString()
        }

        return layout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}