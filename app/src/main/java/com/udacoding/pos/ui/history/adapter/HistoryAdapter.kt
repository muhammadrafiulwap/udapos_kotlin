package com.udacoding.pos.ui.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.udacoding.pos.R
import com.udacoding.pos.databinding.ItemHistoryBinding
import com.udacoding.pos.ui.history.model.DataItem
import com.udacoding.pos.utils.*
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(
    val context: Context,
    val data: List<DataItem>?
    ) : RecyclerView.Adapter<HistoryAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    class viewHolder(val binding: ItemHistoryBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem?) {

            binding.apply {
                textViewCode.text = item?.code
                textViewCustomer.text = item?.customer?.name
                textViewPayment.text = item?.paymentMethod
                textViewPrice.text = toRupiah(item?.totalPrice?.toDouble() ?: 0.0)
                binding.textViewDate.text = formatDateOnly(item?.createdAt)
                binding.textViewTime.text = formatTimeOnly(item?.createdAt)

                Glide.with(context)
                    .load(URLImageCustomer() + item?.customer?.photo)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .error(R.drawable.profile_ex).into(binding.imageView)

            }
        }

    }

}