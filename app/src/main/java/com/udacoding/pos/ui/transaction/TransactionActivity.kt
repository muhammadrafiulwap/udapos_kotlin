package com.udacoding.pos.ui.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.udacoding.pos.R
import com.udacoding.pos.databinding.ActivityTransactionBinding
import com.udacoding.pos.databinding.DialogChoosePaymentBinding

class TransactionActivity : AppCompatActivity() {

    lateinit var binding: ActivityTransactionBinding

    lateinit var dialogChoosePaymentBinding: DialogChoosePaymentBinding

    lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.changePayment.setOnClickListener {
            showChoosePayment()
        }


    }

    private fun showChoosePayment() {
        dialogChoosePaymentBinding =
            DialogChoosePaymentBinding.bind(View.inflate(this, R.layout.dialog_choose_payment, null))
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
}