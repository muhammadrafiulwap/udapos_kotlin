package com.udacoding.pos.network

import com.udacoding.pos.ui.customer.model.ResponseCustomer
import com.udacoding.pos.ui.home.model.ResponseProduct
import com.udacoding.pos.ui.login.model.ResponseLogin
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*

//TODO 2 Declare api service
interface ApiService {

    @GET("product")
    fun apiGetProduct(): Flowable<ResponseProduct>

    @GET("customer")
    fun apiGetCustomer(): Flowable<ResponseCustomer>

    @FormUrlEncoded
    @POST("login")
    fun apiLogin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Single<ResponseLogin>

}