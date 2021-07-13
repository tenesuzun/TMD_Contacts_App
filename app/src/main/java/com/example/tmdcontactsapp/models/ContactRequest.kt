package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class ContactRequest(
    /** This class will be used to upgrade or add new contacts in POST method */
    @SerializedName("Id")
    var contactId: Int,
    @SerializedName("Photo")
    var contactPicture: String,
    @SerializedName("Name")
    var firstName: String,
    @SerializedName("Surname")
    var surname: String,
    @SerializedName("Email")
    var emailAddress: String,
    @SerializedName("Tel")
    var phoneNumber: String,
    @SerializedName("TelBusiness")
    var workNumber: String,
    @SerializedName("TelHome")
    var homePhone: String,
    @SerializedName("Address")
    var address: String,
    @SerializedName("Company")
    var company: String,
    @SerializedName("Title")
    var title: String,
    @SerializedName("Birthdate")
    var birthday: String,
    @SerializedName("Note")
    var notes: String,
    @SerializedName("userId")
    var userId: Int
)
