package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class Contact(
    //region Attributes
    @SerializedName("photo")
    var contactPicture: String,
    @SerializedName("name")
    var firstName: String,
    @SerializedName("surname")
    var surname: String,
    @SerializedName("email")
    var emailAddress: String,
    @SerializedName("tel")
    var phoneNumber: String,
    @SerializedName("telBusiness")
    var workNumber: String,
    @SerializedName("telHome")
    var homePhone: String,
    @SerializedName("address")
    var address: String,
    @SerializedName("company")
    var company: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("birthDate")
    var birthday: String,
    @SerializedName("note")
    var notes: String,
    @SerializedName("group")
    var groups: String
    //endregion
)