package com.udacoding.pos.repository

import com.udacoding.pos.network.Config
import com.udacoding.pos.ui.home.model.ResponseProduct
import com.udacoding.pos.ui.login.model.ResponseLogin
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

}