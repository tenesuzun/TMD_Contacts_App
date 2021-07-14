package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class GroupResponse(
    @SerializedName("id", alternate = ["Id"])
    var groupId: Int,
    @SerializedName("name", alternate = ["Name"])
    var groupName: String,
    @SerializedName("userId", alternate = ["UserId"])
    var userId: Int
    )
