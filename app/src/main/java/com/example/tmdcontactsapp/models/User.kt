package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("Id", alternate = ["id"])
    var Id: Int? = null,
    @SerializedName("Email", alternate = ["email"])
    var Email: String,
    @SerializedName("Status", alternate = ["status"])
    var Status: Boolean = true,
    @SerializedName("Name", alternate = ["name"])
    var Name: String,
    @SerializedName("Surname", alternate = ["surname"])
    var Surname: String,
    @SerializedName("Password", alternate = ["password"])
    var Password: String? = null,
    @SerializedName("Tel", alternate = ["tel"])
    var Tel: String,
    @SerializedName("TelBusiness", alternate = ["telBusiness"])
    var TelBusiness: String,
    @SerializedName("TelHome", alternate = ["telHome"])
    var TelHome: String,
    @SerializedName("Address", alternate = ["address"])
    var Address: String,
    @SerializedName("Photo", alternate = ["photo"])
    var Photo: String? = null,
    @SerializedName("Company", alternate = ["company"])
    var Company: String,
    @SerializedName("Title", alternate = ["title"])
    var Title: String,
    @SerializedName("BirthDate", alternate = ["birthDate"])
    var BirthDate: String,
    @SerializedName("Note", alternate = ["note"])
    var Note: String
)