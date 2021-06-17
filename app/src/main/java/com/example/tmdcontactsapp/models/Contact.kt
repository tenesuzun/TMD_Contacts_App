package com.example.tmdcontactsapp.models

data class Contact(
    //region Attributes
    private var contactPicture: String,
    private var firstName: String,
    private var surname: String,
    private var emailAddress: String,
    private var phoneNumber: String,
    private var workNumber: String,
    private var homePhone: String,
    private var address: String,
    private var company: String,
    private var title: String,
    private var birthday: String,
    private var notes: String,
    private var groups: String,
    //endregion
)