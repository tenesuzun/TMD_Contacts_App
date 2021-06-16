package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var emailField: EditText
    lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpBtn = findViewById<Button>(R.id.loginSignUpButton)
        signUpBtn.setOnClickListener{
            val intent = Intent(this, UserRegistryActivity::class.java)
            startActivity(intent)
        }

        val forgotPasswordBtn = findViewById<TextView>(R.id.loginForgotPassword)
        forgotPasswordBtn.setOnClickListener{
            val intent = Intent(this, forgottenPasswordActivity::class.java)
            startActivity(intent)
        }

        emailField = findViewById(R.id.loginEmailField)
        passwordField = findViewById(R.id.loginPasswordField)

    }

    fun login(view : View){
        if(emailField.text.isEmpty().apply { }){
            Toast.makeText(applicationContext,"Please enter your e-mail",Toast.LENGTH_LONG).show()
        }
        else if(passwordField.text.isEmpty().apply { }){
            Toast.makeText(applicationContext,"Please enter your password",Toast.LENGTH_LONG).show()
        }
        else{
            val intent = Intent(this, ContactsListActivity::class.java)
            startActivity(intent)
        }
    }
}