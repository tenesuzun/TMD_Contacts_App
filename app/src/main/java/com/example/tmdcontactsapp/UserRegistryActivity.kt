package com.example.tmdcontactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class UserRegistryActivity : AppCompatActivity() {

    //region Lateinit declarations
    lateinit var userPP: ImageView
    lateinit var userFirstName: EditText
    lateinit var userSurname: EditText
    lateinit var userPassword: EditText
    lateinit var userEmail: EditText
    lateinit var userPasswordAgain: EditText
    lateinit var userPhone: EditText
    lateinit var userWorkPhone: EditText
    lateinit var userHomePhone: EditText
    lateinit var userAddress: EditText
    lateinit var userCompany: EditText
    lateinit var userWorkTitle: EditText
    lateinit var userBirthday: EditText
    lateinit var userNotes: EditText
    lateinit var signupButton: Button
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registry)

        userFirstName = findViewById(R.id.registryFirstName)
        userSurname = findViewById(R.id.registrySurname)
        userPhone = findViewById(R.id.registryPhoneNumber)
        userEmail = findViewById(R.id.registryEmailAddress)
        userPassword = findViewById(R.id.registryPassword)
        userPasswordAgain = findViewById(R.id.registryPasswordAgain)
    }
}