package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("Email")
    var Email: String,
    @SerializedName("Password")
    var Password: String,
    @SerializedName("Name")
    var Name: String,
    @SerializedName("Surname")
    var Surname: String,
    @SerializedName("Tel")
    var Tel: String,
    @SerializedName("TelBusiness")
    var TelBusiness: String,
    @SerializedName("TelHome")
    var TelHome: String,
    @SerializedName("Address")
    var Address: String,
    @SerializedName("Photo")
    var Photo: String,
    @SerializedName("Company")
    var Company: String,
    @SerializedName("Title")
    var Title: String,
    @SerializedName("BirthDate")
    var BirthDate: String,
    @SerializedName("Note")
    var Note: String
)