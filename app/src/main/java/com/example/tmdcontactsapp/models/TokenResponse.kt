package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    var token: String,
    @SerializedName("expiration")
    var expiration: String
)