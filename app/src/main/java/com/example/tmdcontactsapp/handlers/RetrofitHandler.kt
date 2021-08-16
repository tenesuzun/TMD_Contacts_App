package com.example.tmdcontactsapp.handlers

import com.example.tmdcontactsapp.networks.ApiClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHandler {
    private var BASE_URL: String = "http://tmdcontacts-api.dev.tmd/api/"

    var retrofit: ApiClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiClient::class.java)

    fun changeBaseUrl(tempString: String){
        BASE_URL = tempString
    }
}