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
    fun getUserByEmail(
        @Header("Authorization") Bearer: String,
        @Query("email")email: String): Call<LoggedUserResponse>

    //endregion

    //region Contacts Methods

    @GET("Contacts/GetListByUserId")
    fun getUserContacts(
        @Header("Authorization") Bearer: String,
        @Query("userId")userId: Int): Call<MutableList<Contact>>

    @GET("Contacts/Get")
    fun getContactInformation(
        @Header("Authorization") Bearer: String,
        @Query("id")contactId: Int): Call<Contact>

    //endregion

    //region Groups Methods

    @GET("/api/Groups/GetListByUserId")
    @Headers("accept: */*")
    fun getUserGroups(
        @Header("Authorization") Bearer: String,
        @Query("userId")userId: Int): Call<MutableList<GroupResponse>>

    //endregion

    //region GroupsContacts Methods

    @GET("GroupsContacts/GetListByGroupId")
    @Headers("accept: */*")
    fun getGroupContacts(
        @Header("Authorization") Bearer: String,
        @Query("groupId")groupId: Int): Call<MutableList<Contact>>

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

    @POST("Contacts/Add")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun addNewContact(
        @Header("Authorization") Bearer: String,
        @Body contact: ContactRequest): Call<ResponseContent>

    @POST("Contacts/Update")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun updateContact(
        @Header("Authorization") Bearer: String,
        @Body contact: ContactRequest): Call<ResponseContent>

    @POST("Contacts/Delete")
    @Headers("accept: */*")
    fun deleteContact(
        @Header("Authorization") Bearer: String,
        @Query("id") contactId: Int): Call<ResponseContent>

    //endregion

    //region Groups Methods

    @POST("Groups/Add")
    @Headers("accept: */*","Content-Type: application/json-patch+json")
    fun addNewGroup(
        @Header("Authorization") Bearer: String,
        @Body group: GroupResponse): Call<ResponseContent>

    @POST("Groups/Delete")
    @Headers("accept: */*")
    fun deleteGroup(
        @Header("Authorization") Bearer: String,
        @Query("id") groupId: Int): Call<ResponseContent>

    //endregion

    //region GroupsContacts Methods

    @POST("GroupsContacts/Add")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun addContactToGroup(
        @Header("Authorization") Bearer: String,
        @Body addedContact: GroupsContacts): Call<ResponseContent>

    @POST("GroupsContacts/Delete")
    @Headers("accept: */*", "Content-Type: application/json-patch+json")
    fun deleteContactFromGroup(
        @Header("Authorization") Bearer: String,
        @Body deletedContact: GroupsContacts): Call<ResponseContent>

    //endregion

    //endregion
}