package com.udacoding.pos.repository

import com.udacoding.pos.network.Config
import com.udacoding.pos.ui.history.model.ResponseGetTransaction
import com.udacoding.pos.ui.login.model.ResponseLogin
import com.udacoding.pos.ui.profile.model.ResponseProfit
import com.udacoding.pos.ui.transaction.model.DetailTransaction
import com.udacoding.pos.ui.transaction.model.ResponseTransaction
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RepositoryUser {

    private val compositeDisposable = CompositeDisposable()
    private val api = Config.apiService()

    fun repoLogin(
        email: String?,
        password: String?,
        responHandler: (ResponseLogin) -> Unit,
        errorhandler: (Throwable) -> Unit
    ) {
        compositeDisposable.add(
            api.apiLogin(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    responHandler(it)
                }, {
                    errorhandler(it)
                })
        )
    }

    fun repoProfit(
        id_user: Int?,
        responHandler: (ResponseProfit) -> Unit,
        errorhandler: (Throwable) -> Unit
    ) {
        compositeDisposable.add(
            api.apiGetProfit(id_user ?: 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    responHandler(it)
                }, {
                    errorhandler(it)
                })
        )
    }

    fun repoTransaction(
        id_user: Int?,
        id_customer: Int?,
        payment_method: String?,
        detail_transaction: DetailTransaction?,
        note: String?,
        responHandler: (ResponseTransaction) -> Unit,
        errorhandler: (Throwable) -> Unit
    ) {
        compositeDisposable.add(
            api.apiTransaction(
                id_user ?: 0,
                id_customer ?: 0,
                payment_method ?: "",
                detail_transaction?.id_product,
                detail_transaction?.qty_product,
                note ?: ""
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    responHandler(it)
                }, {
                    errorhandler(it)
                })
        )
    }

    fun repoGetTransaction(
        id: Int?,
        payment_method: String?,
        responHandler: (ResponseGetTransaction) -> Unit,
        errorhandler: (Throwable) -> Unit
    ) {
        compositeDisposable.add(
            api.apiGetTransaction(
                id ?: 0,
                payment_method ?: ""
            ).subscribeOn(Schedulers.io())
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