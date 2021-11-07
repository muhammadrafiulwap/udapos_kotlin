package com.udacoding.pos.ui.transaction

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.udacoding.pos.R
import com.udacoding.pos.SessionManager
import com.udacoding.pos.databinding.ActivityTransactionBinding
import com.udacoding.pos.databinding.DialogChoosePaymentBinding
import com.udacoding.pos.room.model.EntityCart
import com.udacoding.pos.ui.choose_customer.ChooseCustomerActivity
import com.udacoding.pos.ui.home.viewmodel.HomeViewModel
import com.udacoding.pos.ui.scanqr.SqanQRActivity
import com.udacoding.pos.ui.transaction.model.DetailTransaction
import com.udacoding.pos.ui.transaction.model.ResponseTransaction
import com.udacoding.pos.ui.transaction.viewmodel.TransactionViewModel
import com.udacoding.pos.utils.*

private const val STORAGE_REQUEST_CODE = 1

class TransactionActivity : AppCompatActivity() {

    lateinit var binding: ActivityTransactionBinding

    lateinit var viewModel: TransactionViewModel

    lateinit var viewModelHome: HomeViewModel

    lateinit var session: SessionManager

    lateinit var dialogChoosePaymentBinding: DialogChoosePaymentBinding

    lateinit var dialog: BottomSheetDialog

    private var id_customer = 0

    private var id_product = ArrayList<Int>()
    private var qty_product = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        session = SessionManager(this)

        setupPermission()

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        viewModelHome = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModelHome.getCartLocal()

        attachObserve()

        binding.changePayment.setOnClickListener {
            showChoosePayment()
        }

        binding.btnComplete.setOnClickListener {
            actionCheckout()
        }

        binding.textViewSearch.setOnClickListener {

            val intent = Intent(this, ChooseCustomerActivity::class.java)
            startActivityForResult(intent, codeCustomer())

        }

    }

    private fun setupPermission() {
        val permission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_REQUEST_CODE
        )
    }

    private fun attachObserve() {

        with(viewModel) {

            transaction.observe(this@TransactionActivity, { responseTransaction(it) })

            error.observe(
                this@TransactionActivity,
                { showAlert(getString(R.string.error), it.toString(), true) })

            loading.observe(this@TransactionActivity, { actionLoading(it) })

            payment_method_not_found.observe(this@TransactionActivity, Observer {
                showAlert(getString(R.string.required), "Payment method not found!", true)
            })

            id_customer_not_found.observe(this@TransactionActivity, Observer {
                showAlert(getString(R.string.required), "Choose customer first!", true)
            })

            id_user_not_found.observe(this@TransactionActivity, Observer {
                showAlert(getString(R.string.required), "User not found!", true)
            })

            detail_transaction_not_found.observe(this@TransactionActivity, Observer {
                showAlert(getString(R.string.required), "Detail transaction not found!", true)
            })
        }

        with(viewModelHome) {
            data_cart_local.observe(this@TransactionActivity, { detailTransaction(it) })
            total_pay.observe(this@TransactionActivity, { binding.textTotalPay.text = toRupiah(it)})
        }
    }

    private fun detailTransaction(it: List<EntityCart>?) {
        it?.forEach {
            id_product.add(it.id_product ?: 0)
            qty_product.add(it.qty ?: 0)
        }
    }

    private fun actionLoading(it: Boolean?) {
        if (it == true) {
            binding.progressBar.show()
            binding.btnComplete.hide()
        } else {
            binding.progressBar.hide()
            binding.btnComplete.show()
        }
    }

    private fun responseTransaction(it: ResponseTransaction?) {

        if (it?.isSuccess == true) {
            viewModelHome.deleteCartAll()
            val dialog = SuccessCheckoutFragment(
                it.data
            )
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "")
        }

    }

    private fun actionCheckout() {

        val detail_transaction = DetailTransaction(id_product, qty_product)

        viewModel.postTransaction(
            session.id_user,
            id_customer,
            binding.textViewChoosePayment.text.toString(),
            detail_transaction,
            binding.editTextNote.text.toString()
        )
    }

    private fun showDataCustomer(data: Intent?) {
        binding.layoutCustomer.show()
        binding.textViewName.text = data?.getStringExtra("name")
        binding.textViewPhone.text = data?.getStringExtra("phone")
        id_customer = data?.getIntExtra("id", 0) ?: 0
    }

    private fun showChoosePayment() {
        dialogChoosePaymentBinding =
            DialogChoosePaymentBinding.bind(
                View.inflate(
                    this,
                    R.layout.dialog_choose_payment,
                    null
                )
            )
        dialog = BottomSheetDialog(this).apply {
            setContentView(dialogChoosePaymentBinding.root)
            show()
        }
        dialogChoosePaymentBinding.btnCash.setOnClickListener {
            binding.textViewChoosePayment.text = "CASH"
            dialog.dismiss()
        }

        dialogChoosePaymentBinding.btnTransfer.setOnClickListener {
            binding.textViewChoosePayment.text = "TRANSFER"
            dialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == codeCustomer()) {
            if (data != null) {
                showDataCustomer(data)
            }
        }
    }

}