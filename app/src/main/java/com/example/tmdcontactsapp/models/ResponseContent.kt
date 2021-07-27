package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class ResponseContent(
    @SerializedName("data", alternate = ["Data"])
    var data: String = "",
    @SerializedName("success", alternate = ["Success"])
    var success: Boolean,
    @SerializedName("message", alternate=["Message"])
    var message: String
)
