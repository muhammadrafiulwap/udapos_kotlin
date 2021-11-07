package com.udacoding.pos.ui.choose_customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.udacoding.pos.SessionManager
import com.udacoding.pos.databinding.ActivityChooseCustomerBinding
import com.udacoding.pos.ui.customer.model.DataItem
import com.udacoding.pos.ui.customer.viewmodel.CustomerViewModel
import com.udacoding.pos.ui.home.adapter.CustomerAdapter
import com.udacoding.pos.ui.home.viewmodel.HomeViewModel
import com.udacoding.pos.ui.transaction.TransactionActivity
import com.udacoding.pos.utils.codeCustomer
import com.udacoding.pos.utils.showShimmer
import com.udacoding.pos.utils.showToast

class ChooseCustomerActivity : AppCompatActivity() {

    lateinit var bundle: Bundle

    lateinit var session: SessionManager

    lateinit var viewModel: CustomerViewModel

    private lateinit var binding: ActivityChooseCustomerBinding

    private var adapter_customer: CustomerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseCustomerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)

        initView()
        observeData()

    }

    private fun observeData() {

        with(viewModel) {

            customer.observe(this@ChooseCustomerActivity, { showDataCustomer(it.data) })

            error.observe(this@ChooseCustomerActivity, { showError(it) })

            loading.observe(
                this@ChooseCustomerActivity,
                { showShimmer(it, binding.shimmer, binding.recyclerView) })

        }

    }

    private fun showError(it: Throwable?) {
        applicationContext?.let { it1 -> showToast(it1, it?.message) }
    }

    private fun initView() {

        viewModel.getCustomer()

        binding.searchView.doAfterTextChanged {
            adapter_customer?.filter?.filter(it.toString())
        }

    }

    private fun showDataCustomer(it: List<DataItem>?) {

        adapter_customer = applicationContext?.let { it1 ->
            CustomerAdapter(it1, it, object : CustomerAdapter.OnClickListener {

                override fun item(item: DataItem?) {
                    val intent = Intent(this@ChooseCustomerActivity, TransactionActivity::class.java)
                    intent.putExtra("id", item?.id ?: 0)
                    intent.putExtra("name", item?.name.toString())
                    intent.putExtra("phone", item?.phoneNumber.toString())
                    setResult(codeCustomer(), intent)
                    finish()
                }

            })
        }

        binding.recyclerView.adapter = adapter_customer

    }

}