package com.example.tmdcontactsapp.networks

import com.example.tmdcontactsapp.models.Contact
import com.example.tmdcontactsapp.models.LoginResponse
//import com.example.tmdcontactsapp.models.Group
import com.example.tmdcontactsapp.models.UserResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {

    @GET("Contacts/GetAll")
    fun getAllContacts(): Call<List<Contact>>

//    @GET("/Contacts/GetAll")
//    fun getAllGroups(): Call<List<Group>>

    @POST("Auths/Login")
    @Headers("accept: application/json-patch+json","Content-Type: application/json-patch+json")
    fun userLogin(@Body login: UserResponse): Call<LoginResponse>
}