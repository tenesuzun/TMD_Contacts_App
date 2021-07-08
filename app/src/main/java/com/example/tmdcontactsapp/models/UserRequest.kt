package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("Email")
    var Email: String,
    @SerializedName("Password")
    var Password: String
)