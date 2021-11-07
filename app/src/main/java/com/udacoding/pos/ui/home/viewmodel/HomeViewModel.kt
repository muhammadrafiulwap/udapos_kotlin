package com.udacoding.pos.ui.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacoding.pos.repository.RepositoryProduct
import com.udacoding.pos.room.model.EntityCart
import com.udacoding.pos.ui.home.model.ResponseProduct

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryProduct(application.applicationContext)

    private val _product = MutableLiveData<ResponseProduct>()
    val product: LiveData<ResponseProduct> = _product

    private val _product_by_id = MutableLiveData<ResponseProduct>()
    val product_by_id: LiveData<ResponseProduct> = _product_by_id

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count

    private val _total_item_pay = MutableLiveData<Double>()
    val total_item_pay: LiveData<Double> = _total_item_pay

    private val _total_pay = MutableLiveData<Double>()
    val total_pay: LiveData<Double> = _total_pay

    private val _text_total_pay = MutableLiveData<Boolean>()
    val text_total_pay: LiveData<Boolean> = _text_total_pay

    private val _button_plus = MutableLiveData<Boolean>()
    val button_plus: LiveData<Boolean> = _button_plus

    private val _button_minus = MutableLiveData<Boolean>()
    val button_minus: LiveData<Boolean> = _button_minus

    private val _available_on_cart = MutableLiveData<List<EntityCart>>()
    val available_on_cart: LiveData<List<EntityCart>> = _available_on_cart

    private val _data_cart_local = MutableLiveData<List<EntityCart>>()
    val data_cart_local: LiveData<List<EntityCart>> = _data_cart_local

    private val _add_to_cart = MutableLiveData<Boolean>()
    val add_to_cart: LiveData<Boolean> = _add_to_cart

    private val _update_qty_cart = MutableLiveData<Boolean>()
    val update_qty_cart: LiveData<Boolean> = _update_qty_cart

    private val _delete_cart = MutableLiveData<Boolean>()
    val delete_cart: LiveData<Boolean> = _delete_cart

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getProduct(id_produk: Int?){
        _loading.value = true
        repository.repoGetProduct(
            id_produk,
            {
                _loading.value = false
                if (it.isSuccess == true)
                    _product.value = it
            }, {
                _loading.value = false
                _error.value= it
            }
        )
    }

    fun getProductById(id_produk: Int?){
        _loading.value = true
        repository.repoGetProduct(
            id_produk,
            {
                _loading.value = false
                if (it.isSuccess == true)
                    _product_by_id.value = it
            }, {
                _loading.value = false
                _error.value= it
            }
        )
    }

    fun addToCart(entityCart: EntityCart) {
        repository.insertCart(entityCart, {
            when (it) {
                200 -> _add_to_cart.value = true
            }
        }, {
            _error.value = it
        })
    }

    fun checkAvailableOnCart(id_product: Int) {
        repository.getCartByIdProduk(id_product, {
            _available_on_cart.value = it
        }, {
            _error.value = it
        })
    }

    fun actionCount(flag_action: Int, last_count: Int, stock: Int, price: Double) {
        when (flag_action) {
            0 -> {
                _count.value = last_count.minus(1)
                _total_item_pay.value = count.value?.times(price)
                if (last_count in 2..stock) {
                    _button_plus.value = true
                    _button_minus.value = true
                } else if (last_count in 1..2) {
                    _button_minus.value = false
                    _button_plus.value = true
                }
            }
            1 -> {
                _count.value = last_count.plus(1)
                _total_item_pay.value = count.value?.times(price)
                if (last_count >= 0 && last_count < stock - 1) {
                    _button_minus.value = true
                } else if (last_count >= stock - 1) {
                    _button_plus.value = false
                    _button_minus.value = true
                }
            }
        }
    }

    fun getTotalPay() {
        repository.getTotalPay({
            _total_pay.value = it
        }, {
            _error.value = it
        })
    }

    fun getCartLocal() {
        repository.getCart({
            _data_cart_local.value = it
            Log.d("TAG", "getCartLocal: $it")
            if (it.isNotEmpty()) {
                _text_total_pay.value = true
                getTotalPay()
            } else {
                _text_total_pay.value = false
            }
        }, {
            _error.value = it
        })
    }

    fun updateQtyCart(
        id_produk: Int,
        qty: Int,
        total: Double
    ) {
        repository.updateQtyCart(id_produk, qty, total, {
            _update_qty_cart.value = it
        }, {
            _error.value = it
        })
    }

    fun deleteCart(
        id_produk: Int
    ) {
        repository.deleteCart(id_produk, {
            _delete_cart.value = it
        }, {
            _error.value = it
        })
    }

    fun deleteCartAll() {
        repository.deleteCartAll()
    }


}