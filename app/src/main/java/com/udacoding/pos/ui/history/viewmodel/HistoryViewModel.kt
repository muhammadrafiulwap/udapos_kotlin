package com.udacoding.pos.ui.history.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacoding.pos.repository.RepositoryCustomer
import com.udacoding.pos.repository.RepositoryUser
import com.udacoding.pos.ui.customer.model.ResponseCustomer
import com.udacoding.pos.ui.history.model.ResponseGetTransaction

class HistoryViewModel : ViewModel() {

    private val repository = RepositoryUser()

    private val _history = MutableLiveData<ResponseGetTransaction>()
    val history: LiveData<ResponseGetTransaction> = _history

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getTransaction(
        id: Int?,
        payment_method: String?
    ){
        _loading.postValue(true)
        repository.repoGetTransaction(
            id,
            payment_method,
            {
                _loading.value = false
                if (it.isSuccess == true)
                    _history.value = it
            }, {
                _loading.value = false
                _error.value = it
            }
        )
    }
}