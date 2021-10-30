package com.udacoding.pos.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.udacoding.pos.R
import com.udacoding.pos.databinding.ItemCartBinding
import com.udacoding.pos.room.model.EntityCart
import com.udacoding.pos.utils.URLImageProduct
import com.udacoding.pos.utils.toRupiah

class CartAdapter(
    private val context: Context,
    private val data: List<EntityCart?>?
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(context, binding)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bind(item)
    }

    class ViewHolder(val context: Context, val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EntityCart?) {
            binding.textViewNameProduct.text = item?.name_product
            binding.textViewPriceProduct.text = toRupiah(item?.price_product?.toDouble())
            binding.textViewQty.text = "Qty : ${item?.qty}"
            binding.textViewPay.text = toRupiah(item?.total_item_price ?: 0.0)
            Glide.with(context).load(URLImageProduct()+item?.image_product)
                .transform(CenterCrop(), RoundedCorners(10))
                .error(R.drawable.product_ex).into(binding.imageViewProduct)
        }
    }
}