package com.udacoding.pos.network

import com.udacoding.pos.ui.customer.model.ResponseCustomer
import com.udacoding.pos.ui.history.model.ResponseGetTransaction
import com.udacoding.pos.ui.home.model.ResponseProduct
import com.udacoding.pos.ui.login.model.ResponseLogin
import com.udacoding.pos.ui.profile.model.ResponseProfit
import com.udacoding.pos.ui.transaction.model.ResponseTransaction
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

//TODO 2 Declare api service
interface ApiService {

    @GET("product")
    fun apiGetProduct(@Query("id") id: Int?): Flowable<ResponseProduct>

    @GET("customer")
    fun apiGetCustomer(): Flowable<ResponseCustomer>

    @GET("profit")
    fun apiGetProfit(@Query("id_user") id_user: Int): Single<ResponseProfit>

    @GET("transaction")
    fun apiGetTransaction(
        @Query("id") id: Int,
        @Query("payment_method") payment_method: String
    ): Flowable<ResponseGetTransaction>

    @FormUrlEncoded
    @POST("login")
    fun apiLogin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Single<ResponseLogin>

    @FormUrlEncoded
    @POST("transaction")
    fun apiTransaction(
        @Field("id_user") id_user: Int?,
        @Field("id_customer") id_customer: Int?,
        @Field("payment_method") payment_method: String?,
        @Field("id_product[]") id_product: ArrayList<Int>?,
        @Field("qty_product[]") qty_product: ArrayList<Int>?,
        @Field("note") note: String?
    ): Single<ResponseTransaction>

}