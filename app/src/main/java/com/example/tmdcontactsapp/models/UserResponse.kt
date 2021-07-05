package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("Email")
    var Email: String,
    @SerializedName("Password")
    var Password: String
)
