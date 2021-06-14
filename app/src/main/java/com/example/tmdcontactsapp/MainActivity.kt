package com.example.tmdcontactsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpBtn = findViewById<Button>(R.id.loginSignUpButton)
        signUpBtn.setOnClickListener{
            val intent = Intent(this, UserRegistryActivity::class.java)
            startActivity(intent)
        }

        val loginBtn = findViewById<Button>(R.id.loginButton)
        loginBtn.setOnClickListener{
            val intent = Intent(this, ContactsListActivity::class.java)
            startActivity(intent)
        }

        val forgotPasswordBtn = findViewById<TextView>(R.id.loginForgotPassword)
        forgotPasswordBtn.setOnClickListener{
            val intent = Intent(this, forgottenPasswordActivity::class.java)
            startActivity(intent)
        }

    }
}