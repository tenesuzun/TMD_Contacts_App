package com.example.tmdcontactsapp.networks

import com.example.tmdcontactsapp.models.Contact
import com.example.tmdcontactsapp.models.LoginResponse
import com.example.tmdcontactsapp.models.User
//import com.example.tmdcontactsapp.models.Group
import com.example.tmdcontactsapp.models.UserResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {
    //region GET methods
    @GET("Users/GetByEmail")
    fun getUserByEmail(@Query("email")email: String): Call<User>

    @GET("Contacts/GetListByUserId")
    fun getUserContacts(@Query("userId")userId: Int): Call<List<Contact>>
    //endregion

    //region POST methods
    @POST("Auths/Login")
    @Headers("accept: application/json-patch+json","Content-Type: application/json-patch+json")
    fun userLogin(@Body login: UserResponse): Call<LoginResponse>

    @POST("Auths/Register")
    @Headers("accept: */*","Content-Type: application/json-patch+json")
    fun userRegistry(@Body user: User): Call<LoginResponse>
    //endregion
}