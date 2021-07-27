package com.example.tmdcontactsapp.models

import com.google.gson.annotations.SerializedName

data class GroupsContacts(
    @SerializedName("Id", alternate = ["id"])
    var id: Int = 0,
    @SerializedName("GroupId", alternate = ["groupId"])
    var groupId: Int,
    @SerializedName("ContactId", alternate = ["contactId"])
    var contactId: Int
    )
