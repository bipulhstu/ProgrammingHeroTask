package com.bipulhstu.programminghero.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    private val BASE_URL = "https://herosapp.nyc3.digitaloceanspaces.com/"
    private var mInstance: RetrofitClient? = null
    private var retrofit: Retrofit? = null

    private fun RetrofitClient() {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Synchronized
    fun getInstance(): RetrofitClient? {
        if (mInstance == null) {
            mInstance = com.bipulhstu.programminghero.retrofit.RetrofitClient()
        }
        return mInstance
    }

    fun getApi(): ApiConfig? {
        return retrofit!!.create(ApiConfig::class.java)
    }
}