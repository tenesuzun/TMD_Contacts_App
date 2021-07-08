package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class LoggedUserResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("telBusiness")
    val telBusiness: String,
    @SerializedName("telHome")
    val telHome: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("company")
    val company: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("note")
    val note: String
)

