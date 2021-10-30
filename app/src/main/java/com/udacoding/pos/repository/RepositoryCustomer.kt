package com.udacoding.pos.repository

import com.udacoding.pos.network.ApiService
import com.udacoding.pos.network.Config
import com.udacoding.pos.ui.customer.model.ResponseCustomer
import com.udacoding.pos.ui.home.model.ResponseProduct
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RepositoryCustomer {

    private val compositeDisposable = CompositeDisposable()
    private val api = Config.apiService()

    fun repoGetCustomer(
        responHandler: (ResponseCustomer) -> Unit,
        errorhandler: (Throwable) -> Unit
    ) {
        compositeDisposable.add(
            api.apiGetCustomer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    responHandler(it)
                }, {
                    errorhandler(it)
                })
        )
    }

    fun onClear() {
        compositeDisposable.clear()
    }

}