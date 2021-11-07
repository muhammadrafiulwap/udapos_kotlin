package com.udacoding.pos.ui.transaction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacoding.pos.repository.RepositoryUser
import com.udacoding.pos.ui.transaction.model.DetailTransaction
import com.udacoding.pos.ui.transaction.model.ResponseTransaction

class TransactionViewModel: ViewModel() {

    private val repository = RepositoryUser()

    private val _transaction = MutableLiveData<ResponseTransaction>()
    val transaction: LiveData<ResponseTransaction> = _transaction

    private val _id_user_not_found = MutableLiveData<Boolean>()
    val id_user_not_found: LiveData<Boolean> = _id_user_not_found

    private val _id_customer_not_found = MutableLiveData<Boolean>()
    val id_customer_not_found: LiveData<Boolean> = _id_customer_not_found

    private val _payment_method_not_found = MutableLiveData<Boolean>()
    val payment_method_not_found: LiveData<Boolean> = _payment_method_not_found

    private val _detail_transaction_not_found = MutableLiveData<Boolean>()
    val detail_transaction_not_found: LiveData<Boolean> = _detail_transaction_not_found

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun postTransaction(
        id_user: Int?,
        id_customer: Int?,
        payment_method: String?,
        detail_transaction: DetailTransaction?,
        note: String?
    ){
        _loading.value = true
        if (id_user == 0 || id_user == null) {
            _id_user_not_found.value = true
            _loading.value = false
        } else if (id_customer == 0 || id_customer == null) {
            _id_customer_not_found.value = true
            _loading.value = false
        } else if (payment_method == "" || payment_method == null) {
            _payment_method_not_found.value = true
            _loading.value = false
        } else if (detail_transaction == null) {
            _detail_transaction_not_found.value = true
            _loading.value = false
        } else {
            repository.repoTransaction(
                id_user,
                id_customer,
                payment_method,
                detail_transaction,
                note,
                {
                    _loading.value = false
                    _transaction.value = it
                }, {
                    _loading.value = false
                    _error.value = it
                }
            )
        }
    }



}