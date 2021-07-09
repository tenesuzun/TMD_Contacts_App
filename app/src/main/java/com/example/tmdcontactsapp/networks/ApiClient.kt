package com.example.tmdcontactsapp.networks

import com.example.tmdcontactsapp.models.*
//import com.example.tmdcontactsapp.models.GroupResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {
    //region GET methods
    @GET("Users/GetByEmail")
    @Headers("accept: */*")
    fun getUserByEmail(@Query("email")email: String): Call<LoggedUserResponse>

    @GET("Contacts/GetListByUserId")
    fun getUserContacts(@Query("userId")userId: Int): Call<List<Contact>>

    @GET("/api/Groups/GetUserGroups")
    @Headers("accept: */*")
    fun getUserGroups(@Query("userId")userId: Int): Call<List<GroupResponse>>

    //endregion

    //region POST methods
    @POST("Auths/Login")
    @Headers("accept: application/json-patch+json","Content-Type: application/json-patch+json")
    fun userLogin(@Body login: UserRequest): Call<TokenResponse>

    @POST("Auths/Register")
    @Headers("accept: */*","Content-Type: application/json-patch+json")
    fun userRegistry(@Body user: User): Call<TokenResponse>
    //endregion
}