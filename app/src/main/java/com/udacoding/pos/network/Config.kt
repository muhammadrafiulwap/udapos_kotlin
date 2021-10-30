package com.udacoding.pos.network

import com.udacoding.pos.utils.baseURL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Config {

    companion object {

        //TODO 1 Config network access api service
        private fun getOkhttpClient() : OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val builder = OkHttpClient.Builder().addInterceptor(logging).build()

            return builder
        }

        fun getRetrofit() : Retrofit {
            return Retrofit.Builder().baseUrl(baseURL())
                .client(getOkhttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
        }

        fun apiService() : ApiService {
            return getRetrofit().create(ApiService::class.java)
        }

    }

}