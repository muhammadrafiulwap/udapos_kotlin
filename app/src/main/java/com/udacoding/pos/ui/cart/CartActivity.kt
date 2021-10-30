package com.udacoding.pos.ui.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.udacoding.pos.databinding.ActivityCartBinding
import com.udacoding.pos.room.model.EntityCart
import com.udacoding.pos.ui.home.adapter.CartAdapter
import com.udacoding.pos.ui.home.viewmodel.HomeViewModel
import com.udacoding.pos.ui.transaction.TransactionActivity
import com.udacoding.pos.utils.openActivity
import com.udacoding.pos.utils.toRupiah

class CartActivity : AppCompatActivity() {

    lateinit var binding: ActivityCartBinding

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        initView()
        attachObserve()



    }

    private fun showDataCart(it: List<EntityCart>?) {

        binding.list.adapter = CartAdapter(this, it)

    }

    private fun attachObserve() {
        with(viewModel) {
            data_cart_local.observe(this@CartActivity, { showDataCart(it) })

            total_pay.observe(
                this@CartActivity, {
                    binding.textTotalPay.text = toRupiah(it ?: 0.0)
                })

        }
    }

    private fun initView() {

        with(viewModel) {
            getCartLocal()
            getTotalPay()
        }

        with(binding) {
            imageBack.setOnClickListener {
                finish()
            }
            buttonContinue.setOnClickListener {
                openActivity(TransactionActivity::class.java)
            }
            buttonRemoveAll.setOnClickListener {
                viewModel.deleteCartAll()
                finish()
            }
        }

    }
}