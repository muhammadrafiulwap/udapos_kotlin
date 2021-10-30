package com.udacoding.pos.ui.home.adapter

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.udacoding.pos.R
import com.udacoding.pos.databinding.ItemProductBinding
import com.udacoding.pos.ui.home.model.DataItem
import com.udacoding.pos.ui.home.model.DataProduk
import com.udacoding.pos.ui.home.model.ResponseProduct
import com.udacoding.pos.utils.URLImageProduct
import com.udacoding.pos.utils.toRupiah

class ProdukAdapter(
    val context: Context,
    val data: List<DataItem>?,
    val itemClick: OnClickListener
    ) : RecyclerView.Adapter<ProdukAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding, context, itemClick)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        data?.get(position)?.let { holder.bind(it) }

    }

    override fun getItemCount(): Int = data?.size ?: 0

    class viewHolder(val binding: ItemProductBinding, val context: Context, val itemClick: OnClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem?) {

            binding.apply {
                textProduct.text = item?.product
                textDesc.text = item?.description
                textStock.text = "Stock ${item?.stock}"
                textPrice.text = toRupiah(item?.price?.toDouble())
                imageProduct.setImageResource(R.drawable.product_ex)

                Glide.with(context)
                    .load(URLImageProduct() + item?.image)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .error(R.drawable.product_ex).into(binding.imageProduct)

                root.setOnClickListener {
                    itemClick.viewDetail(item)
                }

            }
        }

    }

    interface OnClickListener {
        fun viewDetail(item: DataItem?)
    }

}