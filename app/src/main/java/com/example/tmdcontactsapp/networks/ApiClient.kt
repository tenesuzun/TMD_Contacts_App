package com.example.tmdcontactsapp.networks

import com.example.tmdcontactsapp.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {
    //region GET methods

    //region User methods
    @GET("Users/GetByEmail")
    @Headers("accept: */*")
    fun getUserByEmail(@Query("email")email: String): Call<LoggedUserResponse>
    //endregion

    //region Contacts Methods
    @GET("Contacts/GetListByUserId")
    fun getUserContacts(@Query("userId")userId: Int): Call<MutableList<Contact>>
    //endregion

    //region Groups Methods
    @GET("/api/Groups/GetListByUserId")
    @Headers("accept: */*")
    fun getUserGroups(@Query("userId")userId: Int): Call<MutableList<GroupResponse>>
    //endregion

    //region GroupsContacts Methods
    @GET("GroupsContacts/GetListByGroupId")
    @Headers("accept: */*")
    fun getGroupContacts(@Query("groupId")groupId: Int): Call<MutableList<Contact>>
    //endregion

    //endregion

    //region POST methods

    //region Authentication Methods
    @POST("Auths/Login")
    @Headers("accept: application/json-patch+json","Content-Type: application/json-patch+json")
    fun userLogin(@Body login: UserRequest): Call<TokenResponse>

    @POST("Auths/Register")
    @Headers("accept: */*","Content-Type: application/json-patch+json")
    fun userRegistry(@Body user: User): Call<TokenResponse>

    @POST("Auths/ForgotPassword")
    @Headers("accept: */*")
    fun forgotPassword(@Query("email") email: String): Call<ResponseBody>

    @POST("Auths/ResetPassword")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun resetPassword(@Body email_password: UserRequest): Call<ResponseBody>
    //endregion

    //region Contacts Methods
    //TODO("Gotta correct the response models according to new Response created by Faruk")
    @POST("Contacts/Add")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun addNewContact(@Body contact: ContactRequest): Call<ResponseBody>

    @POST("Contacts/Update")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun updateContact(@Body contact: ContactRequest): Call<ResponseBody>

    @POST("Contacts/Delete")
    @Headers("accept: */*")
    fun deleteContact(@Query("id") contactId: Int): Call<ResponseBody>
    //endregion

    //region Groups Methods
    @POST("Groups/Add")
    @Headers("accept: */*","Content-Type: application/json-patch+json")
    fun addNewGroup(@Body group: GroupResponse): Call<ResponseBody>

    @POST("Groups/Delete")
    @Headers("accept: */*")
    fun deleteGroup(@Query("id") groupId: Int): Call<ResponseBody>
    //endregion

    //region GroupsContacts Methods
    @POST("GroupsContacts/Add")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun addContactToGroup(@Body addedContact: GroupsContacts): Call<ResponseContent>

    @POST("GroupsContacts/Delete")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun deleteContactFromGroup(@Body deletedContact: GroupsContacts): Call<ResponseContent>
    //endregion

    //endregion
}