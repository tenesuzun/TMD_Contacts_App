package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("Email")
    var Email: String,
    @SerializedName("Password")
    var Password: String
)

data class LoginResponse(
    @SerializedName("token")
    var token: String,
    @SerializedName("expiration")
    var expiration: String
)