package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class GroupResponse(
    @SerializedName("id")
    var groupId: Int,
    @SerializedName("name")
    var groupName: String,
    @SerializedName("userId")
    var userId: Int
    )
