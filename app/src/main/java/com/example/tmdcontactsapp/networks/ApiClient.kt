package com.example.tmdcontactsapp.networks

import com.example.tmdcontactsapp.models.Contact
import com.example.tmdcontactsapp.models.Group
import retrofit2.Call
import retrofit2.http.GET

interface ApiClient {

    @GET("/User")
    fun getAllContacts(): Call<List<Contact>>

    @GET("/Contacts/GetAll")
    fun getAllGroups(): Call<List<Group>>
}