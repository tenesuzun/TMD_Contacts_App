package com.example.tmdcontactsapp.networks

import android.text.Html
import com.example.tmdcontactsapp.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {
    //region GET methods
    @GET("Users/GetByEmail")
    @Headers("accept: */*")
    fun getUserByEmail(@Query("email")email: String): Call<LoggedUserResponse>

    @GET("Contacts/GetListByUserId")
    fun getUserContacts(@Query("userId")userId: Int): Call<MutableList<Contact>>

    @GET("/api/Groups/GetListByUserId")
    @Headers("accept: */*")
    fun getUserGroups(@Query("userId")userId: Int): Call<MutableList<GroupResponse>>

    @GET("GroupsContacts/GetListByGroupId")
    @Headers("accept: */*")
    fun getGroupContacts(@Query("groupId")groupId: Int): Call<List<Contact>>

    //endregion

    //region POST methods
    @POST("Auths/Login")
    @Headers("accept: application/json-patch+json","Content-Type: application/json-patch+json")
    fun userLogin(@Body login: UserRequest): Call<TokenResponse>

    @POST("Auths/Register")
    @Headers("accept: */*","Content-Type: application/json-patch+json")
    fun userRegistry(@Body user: User): Call<TokenResponse>

    @POST("Contacts/Add")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun addNewContact(@Body contact: ContactRequest): Call<ResponseBody>

    @POST("Contacts/Update")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun updateContact(@Body contact: ContactRequest): Call<ResponseBody>

    @POST("Groups/Add")
    @Headers("accept: */*","Content-Type: application/json-patch+json")
    fun addNewGroup(@Body group: GroupResponse): Call<ResponseBody>

    @POST("Contacts/Delete")
    @Headers("accept: */*")
    fun deleteContact(@Query("id") contactId: Int): Call<ResponseBody>

    @POST("Groups/Delete")
    @Headers("accept: */*")
    fun deleteGroup(@Query("id") groupId: Int): Call<ResponseBody>

    @POST("Auths/ForgotPassword")
    @Headers("accept: */*")
    fun forgotPassword(@Query("email") email: String): Call<ResponseBody>

    @POST("Auths/ResetPassword")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun resetPassword(@Body email_password: UserRequest): Call<ResponseBody>
    //endregion
}