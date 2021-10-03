package com.udacoding.pos.ui.home.adapter

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacoding.pos.R
import com.udacoding.pos.databinding.ItemProductBinding
import com.udacoding.pos.ui.home.model.DataProduk

class ProdukAdapter(val data: List<DataProduk>?): RecyclerView.Adapter<ProdukAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    class viewHolder(val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataProduk?) {

            binding.apply {
                textProduct.text = item?.produk
                textDesc.text = item?.desc
                textStock.text = "Stock ${item?.stock}"
                textPrice.text = item?.price
                imageProduct.setImageResource(R.drawable.product_ex)
            }
        }

    }

}