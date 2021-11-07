package com.udacoding.pos.ui.home.adapter

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
import com.udacoding.pos.databinding.ItemCustomerBinding
import com.udacoding.pos.ui.customer.model.DataItem
import com.udacoding.pos.utils.URLImageCustomer
import java.util.*
import kotlin.collections.ArrayList

class CustomerAdapter(
    val context: Context,
    val data: List<DataItem>?,
    private val itemClick: OnClickListener
    ) : RecyclerView.Adapter<CustomerAdapter.viewHolder>(), Filterable {

    var dataFilter: List<DataItem?>? = ArrayList()

    init {
        dataFilter = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding =
            ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding, context, itemClick)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        data?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    class viewHolder(val binding: ItemCustomerBinding, val context: Context, val itemClick: OnClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem?) {

            binding.apply {
                textCustomerName.text = item?.name
                textCustomerPhone.text = item?.phoneNumber
                imageCustomer.setImageResource(R.drawable.profile_ex)

                Glide.with(context)
                    .load(URLImageCustomer() + item?.photo)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .error(R.drawable.profile_ex).into(binding.imageCustomer)

                binding.root.setOnClickListener {
                    itemClick.item(item)
                }
            }
        }

    }

    interface OnClickListener {
        fun item(item: DataItem?)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val textSearch = constraint.toString()
                if (textSearch.isEmpty()){
                    if (data != null)
                        dataFilter = data
                } else {
                    val resultList = ArrayList<DataItem>()
                    if (data != null){
                        for (row in data) {
                            if (row.name?.toLowerCase(Locale.ROOT)?.contains(textSearch.toLowerCase(
                                    Locale.ROOT)) == true
                                || row.phoneNumber?.toLowerCase(Locale.ROOT)?.contains(textSearch.toLowerCase(
                                    Locale.ROOT)) == true
                            ) {
                                resultList.add(row)
                            }
                        }
                    }
                    dataFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataFilter = results?.values as ArrayList<DataItem>
                notifyDataSetChanged()
            }

        }

    }

}