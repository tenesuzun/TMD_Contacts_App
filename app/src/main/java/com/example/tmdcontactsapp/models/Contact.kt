package com.example.tmdcontactsapp.models

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.example.tmdcontactsapp.R

data class Contact(
    //region Attributes
    var contactPicture: Int,
    var firstName: String,
    var surname: String,
    private var emailAddress: String = "",
    private var phoneNumber: String = "",
    private var workNumber: String = "",
    private var homePhone: String = "",
    private var address: String = "",
    private var company: String = "",
    private var title: String = "",
    private var birthday: String = "",
    private var notes: String = "",
    private var groups: String = "",
    //endregion
)