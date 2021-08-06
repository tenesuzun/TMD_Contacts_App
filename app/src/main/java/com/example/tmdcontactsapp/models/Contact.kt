package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class Contact(
    /** This class is used to get Contacts list for logged in user */
    //region Attributes
    @SerializedName("id", alternate = ["Id"])
    var contactId: Int,
    @SerializedName("photo", alternate = ["Photo"])
    var contactPicture: String,
    @SerializedName("name", alternate = ["Name"])
    var firstName: String,
    @SerializedName("surname", alternate = ["Surname"])
    var surname: String,
    @SerializedName("email", alternate = ["Email"])
    var emailAddress: String,
    @SerializedName("tel", alternate = ["Tel"])
    var phoneNumber: String,
    @SerializedName("telBusiness", alternate = ["TelBusiness"])
    var workNumber: String,
    @SerializedName("telHome", alternate = ["TelHome"])
    var homePhone: String,
    @SerializedName("address", alternate = ["Address"])
    var address: String,
    @SerializedName("company", alternate = ["Company"])
    var company: String,
    @SerializedName("title", alternate = ["Title"])
    var title: String,
    @SerializedName("birthDate", alternate = ["Birthdate"])
    var birthday: String,
    @SerializedName("note", alternate = ["Note"])
    var notes: String,
    @SerializedName("group", alternate = ["Group"])
    var groups: String,
    @SerializedName("userId", alternate = ["UserId"])
    var userId: Int
    //endregion
)