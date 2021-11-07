package com.udacoding.pos.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.udacoding.pos.R
import com.udacoding.pos.SessionManager
import com.udacoding.pos.databinding.FragmentDetailProductBinding
import com.udacoding.pos.databinding.FragmentHomeBinding
import com.udacoding.pos.databinding.FragmentScanBinding
import com.udacoding.pos.room.model.EntityCart
import com.udacoding.pos.ui.cart.CartActivity
import com.udacoding.pos.ui.home.adapter.ProdukAdapter
import com.udacoding.pos.ui.home.model.DataItem
import com.udacoding.pos.ui.home.viewmodel.HomeViewModel
import com.udacoding.pos.ui.scanqr.SqanQRActivity
import com.udacoding.pos.utils.*

private const val CAMERA_REQUEST_CODE = 1

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    lateinit var session: SessionManager

    lateinit var fragmentDetailProduct: FragmentDetailProductBinding

    lateinit var fragmentScan: FragmentScanBinding

    lateinit var bottomSheet: BottomSheetDialog

    lateinit var bottomSheetScan: BottomSheetDialog

    lateinit var viewModel: HomeViewModel

    private var flag_update: Boolean? = null

    private var last_count: Int? = null

    private var pay_total_item: Double? = null

    private var item_product: DataItem? = null

    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = SessionManager(view.context)

        initView(view)
        attachObserve()

    }

    private fun setupPermission(view: View) {
        val permission = ContextCompat.checkSelfPermission(view.context, Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            scanQR(view)
        }
    }

    private fun makeRequest() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it, arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }
    }


    private fun showProduct(data: List<DataItem>?) {
        binding.listProduct.adapter = context?.let {
            ProdukAdapter(it, data, object : ProdukAdapter.OnClickListener {
                override fun viewDetail(item: DataItem?) {
                    item_product = item
                    viewModel.checkAvailableOnCart(item?.id ?: 0)
                }
            })
        }
    }

    private fun showAddToCart(
        item: DataItem?,
        id_cart: Int,
        qty: Int,
        total: Double
    ) {

        fragmentDetailProduct =
            FragmentDetailProductBinding.bind(
                View.inflate(
                    context,
                    R.layout.fragment_detail_product,
                    null
                )
            )

        bottomSheet = context?.let {
            BottomSheetDialog(it).apply {
                setContentView(fragmentDetailProduct.root)
                show()
            }
        } ?: return

        with(fragmentDetailProduct) {

            Glide.with(root)
                .load(URLImageProduct() + item?.image)
                .transform(CenterCrop(), RoundedCorners(10))
                .error(R.drawable.product_ex).into(fragmentDetailProduct.imageProduct)

            if (item?.stock == 0) {

                textCount.text = getString(R.string.stock_kosong)
                buttonAdd.hide()
                buttonMin.hide()
                textTotal.text = toRupiah(item.price?.toDouble() ?: 0.0)
                button.text = getString(R.string.back_to_list)
                button.setOnClickListener {
                    bottomSheet.dismiss()
                }

            } else if (id_cart > 0) {

                if (item?.stock == 1) {
                    buttonAdd.hide()
                }

                flag_update = true
                last_count = qty
                textCount.text = qty.toString()
                textTotal.text = toRupiah(total)

                button.text = getString(R.string.update_cart)
                button.setOnClickListener {
                    viewModel.updateQtyCart(
                        item?.id ?: 0,
                        last_count ?: 0,
                        pay_total_item ?: 0.0
                    )
                }

            } else {

                if (item?.stock == 1) {
                    buttonAdd.hide()
                }

                flag_update = false
                last_count = 1
                textTotal.text =
                    toRupiah(item?.price?.toDouble() ?: 0.0)

                pay_total_item = item?.price?.toDouble() ?: 0.0

                buttonMin.hide()

                button.setOnClickListener {
                    viewModel.addToCart(
                        EntityCart(
                            null,
                            1,
                            null,
                            item?.id,
                            item?.product,
                            item?.price.toString(),
                            item?.image.toString(),
                            last_count,
                            pay_total_item
                        )
                    )
                }

            }

            textProduct.text = item?.product
            textDesc.text = item?.description
            textPrice.text =
                toRupiah(item?.price?.toDouble() ?: 0.0)
            textStock.text = "stock(${item?.stock})"

            buttonAdd.setOnClickListener {
                viewModel.actionCount(
                    1,
                    last_count ?: 0,
                    item?.stock ?: 0,
                    item?.price?.toDouble() ?: 0.0
                )
            }

            buttonMin.setOnClickListener {
                viewModel.actionCount(
                    0,
                    last_count ?: 0,
                    item?.stock ?: 0,
                    item?.price?.toDouble() ?: 0.0
                )
            }

        }
    }

    private fun scanQR(view: View) {

        fragmentScan =
            FragmentScanBinding.bind(
                View.inflate(
                    context,
                    R.layout.fragment_scan,
                    null
                )
            )

        codeScanner = CodeScanner(view.context, fragmentScan.scannerView)

        codeScanner.startPreview()

        bottomSheetScan = context?.let {
            BottomSheetDialog(it).apply {
                setContentView(fragmentScan.root)
                show()
            }
        } ?: return

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {

                activity?.runOnUiThread {
//                    Toast.makeText(context, it.text, Toast.LENGTH_SHORT).show()
                    viewModel.getProductById(it.text.toInt())
                    codeScanner.stopPreview()
                    bottomSheetScan.dismiss()
                }
            }

            errorCallback = ErrorCallback {
                activity?.runOnUiThread {
                    Log.e("startScan:", "Camera init error: ${it.message}")
                }
            }
        }

    }


    private fun changeButtonAdd(it: Int?) {
        when (it) {
            0 -> {
                if (flag_update == true) {
                    with(fragmentDetailProduct) {
                        button.text = getString(R.string.delete_from_cart)
                        button.setBackgroundColor(Color.RED)
                        button.setTextColor(Color.WHITE)
                        button.setOnClickListener {
                            viewModel.deleteCart(
                                item_product?.id ?: 0
                            )
                        }
                    }
                } else {
                    with(fragmentDetailProduct) {
                        button.text = getString(R.string.back_to_list)
                        button.setOnClickListener {
                            bottomSheet.dismiss()
                        }
                    }
                }
            }
            else -> {
                if (flag_update == true) {
                    with(fragmentDetailProduct) {
                        button.text = getString(R.string.update_cart)
                        button.setBackgroundColor(resources.getColor(R.color.main_color))
                        button.setTextColor(Color.WHITE)
                        button.setOnClickListener {
                            viewModel.updateQtyCart(
                                item_product?.id ?: 0,
                                last_count ?: 0,
                                pay_total_item ?: 0.0
                            )
                        }
                    }
                } else {
                    with(fragmentDetailProduct) {
                        button.text = getString(R.string.add_to_cart)
                        button.setBackgroundColor(resources.getColor(R.color.main_color))
                        button.setTextColor(Color.WHITE)
                        button.setOnClickListener {
                            viewModel.addToCart(
                                EntityCart(
                                    null,
                                    1,
                                    null,
                                    item_product?.id,
                                    item_product?.product,
                                    item_product?.price.toString(),
                                    item_product?.image.toString(),
                                    last_count,
                                    pay_total_item
                                )
                            )

                        }
                    }
                }

            }
        }

    }

    private fun buttonAdd(it: Boolean?) {
        if (it == true)
            fragmentDetailProduct.buttonAdd.show()
        else
            fragmentDetailProduct.buttonAdd.hide()
    }

    private fun buttonMin(it: Boolean?) {
        if (it == true)
            fragmentDetailProduct.buttonMin.show()
        else
            fragmentDetailProduct.buttonMin.hide()
    }

    private fun actionAvailableCart(it: List<EntityCart>?) {
        if (it?.isNotEmpty() == true)
            showAddToCart(
                item_product,
                it.get(0).id ?: 0,
                it.get(0).qty ?: 0,
                it.get(0).total_item_price ?: 0.0
            )
        else
            showAddToCart(
                item_product,
                0,
                0,
                0.0
            )
    }

    private fun attachObserve() {

        with(viewModel) {
            product.observe(viewLifecycleOwner, { showProduct(it?.data) })

            product_by_id.observe(viewLifecycleOwner, {
                item_product = it?.data?.get(0)
                viewModel.checkAvailableOnCart(it?.data?.get(0)?.id ?: 0)
            })

            error.observe(viewLifecycleOwner, { showError(activity?.applicationContext, it) })

            count.observe(viewLifecycleOwner, {
                last_count = it
                fragmentDetailProduct.textCount.text = last_count.toString()

                changeButtonAdd(it)
            })

            total_item_pay.observe(
                viewLifecycleOwner,
                {
                    pay_total_item = it
                    fragmentDetailProduct.textTotal.text = toRupiah(it)
                })

            button_plus.observe(viewLifecycleOwner, { buttonAdd(it) })

            button_minus.observe(viewLifecycleOwner, Observer { buttonMin(it) })

            add_to_cart.observe(viewLifecycleOwner, Observer {
                bottomSheet.dismiss()
                getCartLocal()
                activity?.applicationContext?.let {
                    showToast(
                        it,
                        "Success add to Cart"
                    )
                }
            })

            total_pay.observe(
                viewLifecycleOwner,
                { binding.textTotalPay.text = toRupiah(it ?: 0.0) }
            )

            text_total_pay.observe(viewLifecycleOwner, {
                if (it == true)
                    binding.cardTotalPay.show()
                else
                    binding.cardTotalPay.hide()
            })

            available_on_cart.observe(viewLifecycleOwner, {
                actionAvailableCart(it)
            })

            update_qty_cart.observe(viewLifecycleOwner, {
                if (it) {
                    bottomSheet.dismiss()
                    getCartLocal()
                    activity?.applicationContext?.let {
                        showToast(
                            it,
                            getString(R.string.success_update_cart)
                        )
                    }
                }
            })

            delete_cart.observe(viewLifecycleOwner, {
                if (it) {
                    bottomSheet.dismiss()
                    getCartLocal()
                    activity?.applicationContext?.let {
                        showToast(
                            it,
                            getString(R.string.success_delete_cart)
                        )
                    }
                }

            })
        }
    }

    private fun initView(view: View) {

        with(viewModel) {
            getProduct(null)
            getTotalPay()
        }

        with(binding) {
            textFullName.text = session.full_name
            textEmail.text = session.email_user
            cardTotalPay.setOnClickListener {
                activity?.openActivity(CartActivity::class.java)
            }

            fabScan.setOnClickListener {

                setupPermission(view)

            }
        }
    }

    override fun onResume() {
        super.onResume()

        with(viewModel){
            getCartLocal()
            getTotalPay()
        }
    }

}