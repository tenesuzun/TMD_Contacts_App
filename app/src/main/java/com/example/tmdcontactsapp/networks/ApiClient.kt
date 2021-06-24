package com.example.tmdcontactsapp.networks

import com.example.tmdcontactsapp.models.Contact
import retrofit2.Call
import retrofit2.http.GET

interface ApiClient {

    @GET("/User")
    fun getAllContacts(): Call<List<Contact>>
}