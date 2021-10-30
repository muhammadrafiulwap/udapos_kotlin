package com.udacoding.pos.ui.customer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacoding.pos.repository.RepositoryCustomer
import com.udacoding.pos.ui.customer.model.ResponseCustomer

class CustomerViewModel : ViewModel() {

    private val repository = RepositoryCustomer()

    private val _customer = MutableLiveData<ResponseCustomer>()
    val customer: LiveData<ResponseCustomer> = _customer

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getCustomer(){
        _loading.postValue(true)
        repository.repoGetCustomer(
            {
                _loading.postValue(false)
                if (it.isSuccess == true)
                    _customer.postValue(it)
            }, {
                _loading.postValue(false)
                _error.postValue(it)
            }
        )
    }
}