package com.udacoding.pos.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.udacoding.pos.R
import com.udacoding.pos.databinding.ItemCustomerBinding
import com.udacoding.pos.ui.customer.model.DataItem
import com.udacoding.pos.utils.URLImageCustomer

class CustomerAdapter(
    val context: Context,
    val data: List<DataItem>?
    ) : RecyclerView.Adapter<CustomerAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding =
            ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    class viewHolder(val binding: ItemCustomerBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem?) {

            binding.apply {
                textCustomerName.text = item?.name
                textCustomerPhone.text = item?.phoneNumber
                imageCustomer.setImageResource(R.drawable.profile_ex)

                Glide.with(context)
                    .load(URLImageCustomer() + item?.photo)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .error(R.drawable.profile_ex).into(binding.imageCustomer)
            }
        }

    }

}