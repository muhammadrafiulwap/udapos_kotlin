package com.udacoding.pos.ui.customer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacoding.pos.databinding.FragmentCustomerBinding
import com.udacoding.pos.ui.customer.model.DataItem
import com.udacoding.pos.ui.customer.model.responseCustomer
import com.udacoding.pos.ui.customer.viewmodel.CustomerViewModel
import com.udacoding.pos.ui.home.adapter.CustomerAdapter
import com.udacoding.pos.utils.showError

class CustomerFragment : Fragment() {

    lateinit var binding: FragmentCustomerBinding

    lateinit var viewModel: CustomerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)

        binding = FragmentCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        attachObserve()

    }

    private fun showCustomer(data: List<DataItem>?) {
        binding.listCustomer.adapter = context?.let { CustomerAdapter(it, data) }
    }

    private fun attachObserve() {

        with(viewModel) {
            customer.observe(viewLifecycleOwner, { showCustomer(it?.data) })
            error.observe(viewLifecycleOwner, { showError(activity?.applicationContext, it)})
        }

    }

    private fun initView() {

        with(viewModel) {
            getCustomer()
        }
    }

}